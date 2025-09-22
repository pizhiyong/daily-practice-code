package com.pizhiyong.dailypractice.messagequeue;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平队列系统 - 基于轮询调度确保多租户公平性
 * 
 * 设计思路：
 * 1. 每个客户端维护独立的消息队列
 * 2. 使用循环调度器确保各客户端公平获得处理机会
 * 3. 线程安全设计，支持并发操作
 */
public class FairQueueSystem<T> {
    
    // 存储每个客户端的专用队列
    private final Map<String, Queue<T>> clientQueues = new ConcurrentHashMap<>();
    
    // 循环调度器队列，存储客户端ID
    private final Queue<String> roundRobinScheduler = new LinkedList<>();
    
    // 主锁，保护调度器和队列的一致性操作
    private final ReentrantLock mainLock = new ReentrantLock();
    
    // 统计信息
    private volatile long totalMessages = 0;
    private volatile long processedMessages = 0;
    
    /**
     * 消息类，包装实际消息内容
     */
    public static class Message<T> {
        private final String clientId;
        private final T content;
        private final long timestamp;
        
        public Message(String clientId, T content) {
            this.clientId = clientId;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public String getClientId() { return clientId; }
        public T getContent() { return content; }
        public long getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("Message{clientId='%s', content=%s, timestamp=%d}", 
                               clientId, content, timestamp);
        }
    }
    
    /**
     * 插入消息到队列
     * 
     * @param clientId 客户端ID
     * @param message 消息内容
     * @return 是否成功插入
     */
    public boolean enqueue(String clientId, T message) {
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new IllegalArgumentException("客户端ID不能为空");
        }
        
        mainLock.lock();
        try {
            // 获取或创建客户端专用队列
            Queue<T> clientQueue = clientQueues.computeIfAbsent(clientId, k -> new LinkedList<>());
            
            // 检查客户端是否已在轮询调度器中
            boolean isClientInScheduler = roundRobinScheduler.contains(clientId);
            
            // 添加消息到客户端队列
            boolean added = clientQueue.offer(message);
            
            if (added) {
                totalMessages++;
                
                // 如果客户端不在调度器中，则添加到调度器末尾
                if (!isClientInScheduler) {
                    roundRobinScheduler.offer(clientId);
                    System.out.println(String.format("新客户端 '%s' 加入调度器", clientId));
                }
                
                System.out.println(String.format("消息入队：客户端=%s, 消息=%s, 队列长度=%d", 
                                 clientId, message, clientQueue.size()));
            }
            
            return added;
        } finally {
            mainLock.unlock();
        }
    }
    
    /**
     * 从队列中获取下一条消息进行处理
     * 
     * @return 下一条消息，如果没有消息则返回null
     */
    public Message<T> dequeue() {
        mainLock.lock();
        try {
            // 如果调度器为空，说明没有待处理的客户端
            if (roundRobinScheduler.isEmpty()) {
                return null;
            }
            
            // 从调度器中获取下一个客户端ID
            String currentClientId = roundRobinScheduler.poll();
            
            // 获取该客户端的队列
            Queue<T> clientQueue = clientQueues.get(currentClientId);
            
            if (clientQueue == null || clientQueue.isEmpty()) {
                // 队列不存在或为空，客户端自然退出轮转
                System.out.println(String.format("客户端 '%s' 队列为空，退出调度器", currentClientId));
                if (clientQueue != null && clientQueue.isEmpty()) {
                    clientQueues.remove(currentClientId);
                }
                // 递归尝试下一个客户端
                return dequeue();
            }
            
            // 从客户端队列中取出一条消息
            T messageContent = clientQueue.poll();
            processedMessages++;
            
            // 检查该客户端是否还有更多消息待处理
            if (!clientQueue.isEmpty()) {
                // 如果还有消息，将客户端ID放回调度器末尾
                roundRobinScheduler.offer(currentClientId);
            } else {
                // 队列为空，客户端退出轮转
                clientQueues.remove(currentClientId);
                System.out.println(String.format("客户端 '%s' 所有消息处理完毕，退出调度器", currentClientId));
            }
            
            Message<T> message = new Message<>(currentClientId, messageContent);
            System.out.println(String.format("消息出队：%s, 客户端剩余消息=%d", 
                             message, clientQueue.size()));
            
            return message;
        } finally {
            mainLock.unlock();
        }
    }
    
    /**
     * 批量出队，用于批处理场景
     * 
     * @param batchSize 批次大小
     * @return 消息列表
     */
    public List<Message<T>> dequeueBatch(int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("批次大小必须大于0");
        }
        
        List<Message<T>> batch = new ArrayList<>(batchSize);
        
        for (int i = 0; i < batchSize; i++) {
            Message<T> message = dequeue();
            if (message == null) {
                break; // 没有更多消息
            }
            batch.add(message);
        }
        
        return batch;
    }
    
    /**
     * 获取队列状态信息
     */
    public QueueStatus getStatus() {
        mainLock.lock();
        try {
            Map<String, Integer> clientQueueSizes = new HashMap<>();
            int totalPendingMessages = 0;
            
            for (Map.Entry<String, Queue<T>> entry : clientQueues.entrySet()) {
                int size = entry.getValue().size();
                clientQueueSizes.put(entry.getKey(), size);
                totalPendingMessages += size;
            }
            
            return new QueueStatus(
                clientQueues.size(),
                roundRobinScheduler.size(),
                totalPendingMessages,
                totalMessages,
                processedMessages,
                new ArrayList<>(roundRobinScheduler),
                clientQueueSizes
            );
        } finally {
            mainLock.unlock();
        }
    }
    
    /**
     * 队列状态信息类
     */
    public static class QueueStatus {
        private final int activeClients;
        private final int clientsInScheduler;
        private final int totalPendingMessages;
        private final long totalMessages;
        private final long processedMessages;
        private final List<String> schedulerOrder;
        private final Map<String, Integer> clientQueueSizes;
        
        public QueueStatus(int activeClients, int clientsInScheduler, int totalPendingMessages,
                          long totalMessages, long processedMessages, List<String> schedulerOrder,
                          Map<String, Integer> clientQueueSizes) {
            this.activeClients = activeClients;
            this.clientsInScheduler = clientsInScheduler;
            this.totalPendingMessages = totalPendingMessages;
            this.totalMessages = totalMessages;
            this.processedMessages = processedMessages;
            this.schedulerOrder = schedulerOrder;
            this.clientQueueSizes = clientQueueSizes;
        }
        
        // Getters
        public int getActiveClients() { return activeClients; }
        public int getClientsInScheduler() { return clientsInScheduler; }
        public int getTotalPendingMessages() { return totalPendingMessages; }
        public long getTotalMessages() { return totalMessages; }
        public long getProcessedMessages() { return processedMessages; }
        public List<String> getSchedulerOrder() { return schedulerOrder; }
        public Map<String, Integer> getClientQueueSizes() { return clientQueueSizes; }
        
        @Override
        public String toString() {
            return String.format(
                "QueueStatus{活跃客户端=%d, 调度器中客户端=%d, 待处理消息=%d, " +
                "总消息数=%d, 已处理=%d, 调度顺序=%s, 客户端队列大小=%s}",
                activeClients, clientsInScheduler, totalPendingMessages,
                totalMessages, processedMessages, schedulerOrder, clientQueueSizes
            );
        }
    }
    
    /**
     * 清空所有队列
     */
    public void clear() {
        mainLock.lock();
        try {
            clientQueues.clear();
            roundRobinScheduler.clear();
            totalMessages = 0;
            processedMessages = 0;
            System.out.println("队列已清空");
        } finally {
            mainLock.unlock();
        }
    }
    
    /**
     * 检查队列是否为空
     */
    public boolean isEmpty() {
        mainLock.lock();
        try {
            return roundRobinScheduler.isEmpty();
        } finally {
            mainLock.unlock();
        }
    }
}

/**
 * 示例和测试类
 */
class FairQueueDemo {
    
    public static void main(String[] args) {
        // 创建公平队列系统
        FairQueueSystem<String> fairQueue = new FairQueueSystem<>();
        
        System.out.println("=== 公平队列系统演示 ===\n");
        
        // 模拟多租户场景
        simulateMultiTenantScenario(fairQueue);
        
        // 演示批处理
        System.out.println("\n=== 批处理演示 ===");
        demonstrateBatchProcessing(fairQueue);
    }
    
    private static void simulateMultiTenantScenario(FairQueueSystem<String> fairQueue) {
        // 租户1发送大量消息
        System.out.println("租户1发送1000条消息...");
        for (int i = 1; i <= 1000; i++) {
            fairQueue.enqueue("tenant1", "tenant1_message_" + i);
        }
        
        // 租户2发送少量消息
        System.out.println("租户2发送5条消息...");
        for (int i = 1; i <= 5; i++) {
            fairQueue.enqueue("tenant2", "tenant2_message_" + i);
        }
        
        // 租户3发送中等数量消息
        System.out.println("租户3发送50条消息...");
        for (int i = 1; i <= 50; i++) {
            fairQueue.enqueue("tenant3", "tenant3_message_" + i);
        }
        
        System.out.println("\n当前队列状态：");
        System.out.println(fairQueue.getStatus());
        
        // 模拟消费者处理消息
        System.out.println("\n开始公平处理消息（每个租户轮流处理）：");
        
        Map<String, Integer> processedCount = new HashMap<>();
        
        // 处理前20条消息，观察公平性
        for (int i = 0; i < 20; i++) {
            FairQueueSystem.Message<String> message = fairQueue.dequeue();
            if (message != null) {
                processedCount.merge(message.getClientId(), 1, Integer::sum);
                System.out.println(String.format("处理消息 %d: %s", i + 1, message));
            } else {
                break;
            }
        }
        
        System.out.println("\n前20条消息的处理统计：");
        processedCount.forEach((clientId, count) -> 
            System.out.println(String.format("  %s: %d条消息", clientId, count)));
        
        System.out.println("\n处理后队列状态：");
        System.out.println(fairQueue.getStatus());
    }
    
    private static void demonstrateBatchProcessing(FairQueueSystem<String> fairQueue) {
        // 批量处理剩余消息
        List<FairQueueSystem.Message<String>> batch = fairQueue.dequeueBatch(10);
        
        System.out.println(String.format("批量获取了 %d 条消息：", batch.size()));
        batch.forEach(message -> 
            System.out.println(String.format("  - %s", message)));
        
        System.out.println("\n最终队列状态：");
        System.out.println(fairQueue.getStatus());
    }
}