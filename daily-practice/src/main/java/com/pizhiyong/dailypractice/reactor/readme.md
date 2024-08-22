响应式编程框架-reactor
Reactor 是一个响应式编程框架，它提供了一种基于事件驱动的编程模型，用于处理异步和基于事件的数据流。Reactor 提供了丰富的操作符，用于处理各种类型的数据流，如事件流、数据流、错误流和完成信号。
Reactor 提供了以下核心概念：
1. Flux：一个用于处理异步和基于事件的数据流的类。它支持背压（backpressure），允许消费者控制生产者的速度。
2. Mono：一个用于处理异步和基于事件的单个值的类。它也支持背压，但只支持一个值。
Reactor 提供了丰富的操作符，用于处理各种类型的数据流，如事件流、数据流、错误流和完成信号。以下是一些常见的操作符：
   - map：对每个元素执行一个函数，并将结果作为新的元素发出。
     - filter：根据一个条件过滤元素。
     - flatMap：将每个元素转换为一个流，并将结果合并为一个新的流。
     - concat：将多个流合并为一个新的流。
     - zip：将多个流合并为一个新的流，并使用一个函数将每个流的元素组合成一个新的元素。
     - reduce：将流中的元素合并为一个值。
     - take：限制流的元素数量。
     - takeUntil：当一个条件满足时，停止接收元素。
     - takeWhile：当一个条件满足时，停止接收元素。
     - takeLast：接收最后n个元素。
     - takeFirst：接收第一个元素。
     - takeLastOrDefault：接收最后n个元素，如果没有元素，则返回默认值。
     - takeFirstOrDefault：接收第一个元素，如果没有元素，则返回默认值。
     - takeWhileOrDefault：接收第一个满足条件的元素，如果没有元素，则返回默认值

引入流程：
1. 添加依赖：在 pom.xml 文件中添加以下依赖：
<dependency>
   <groupId>io.projectreactor</groupId>
   <artifactId>reactor-core</artifactId>
   <version>3.4.11</version>
</dependency>
   2. 使用：
     import reactor.core.publisher.Flux;
       public class Main {
         public static void main(String[] args) { 
         Flux.range(1, 5)
         .map(i -> i * i)
         .subscribe(System.out::println);
        }
      }
      输出：1, 4, 9, 16, 25
      这个例子中，我们首先创建了一个从1到5的整数流，然后使用map操作符对每个元素进行平方运算，最后使用subscribe方法订阅流，并将结果打印到控制台。
