package com.pizhiyong.dailypractice.reactor;

import com.pizhiyong.dailypractice.dao.UserMapper;
import com.pizhiyong.dailypractice.entity.User;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * 基于reactor框架的测试类
 *
 * @author: tiantipi
 * @create: 2024−09-01 18:07:39
 * @version: 1.0
 */
@Service
public class ReactorService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private View error;

    public Mono<User> findUserById(Long id) {
        return Mono.fromCallable(() -> userMapper.selectByPrimaryKey(id))
                .delayElement(Duration.ofMillis(500))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<User> findAllUsers() {
        return Flux.fromIterable(userMapper.selectAll())
                .delayElements(Duration.ofMillis(500))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<User> findUsersOlderThan(Integer age) {
        return Flux.fromIterable(userMapper.selectAll())
                .filter(user -> user.getAge() > age)
                .map(user -> User.of(user.getId(), user.getName().toUpperCase(), user.getAge(), user.getSex()))
                .delayElements(Duration.ofMillis(500))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * reactor的背压概念：
     * 背压（Backpressure）是 Reactive Streams 的一个重要机制，主要用于解决生产者产生数据的速度超过消费者处理能力的问题。
     * 通过背压，消费者可以请求生产者发送它能够处理的数量，从而防止数据过载。
     */
    public void backpressure() throws InterruptedException {
        // 模拟一个快速发布者，它产生 1000 个整数，并使用 doOnRequest 和 doOnNext 方法打印出请求和消费的次数。
        Flux<Integer> fastPublisher = Flux.range(1, 1000)
                .doOnRequest(request -> System.out.println("Requested: " + request))
                .map(c -> c + 1) // 对Flux中的原数据进行变换
                .doOnNext(c -> System.out.println("Processing: " + c))// 副作用处理，比如打印发布参数或者消费参数
                .subscribeOn(Schedulers.parallel());// 在并行调度器上发布

        // 订阅者，它使用 onBackpressureBuffer 方法缓冲 10 个元素，并使用 publishOn 方法切换到单线程执行器，
        fastPublisher.onBackpressureBuffer(10)
                .publishOn(Schedulers.single(), 2)// 每次只能处理两个元素
                .doOnNext(c -> {
                    System.out.println("consumed:" + c);
                }).subscribe(new Subscriber<>() {
                    private Subscription subscription;
                    private int count = 0;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        // 手动请求指定个数元素（只会出请求这么多元素，比如发布了1000个，但只指定了200个，也就只会处理200个）
                        this.subscription.request(200);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("consumed1:" + integer);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("error:" + error);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("completed");
                    }
                });
        Thread.sleep(10000);
    }
}
