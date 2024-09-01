package com.pizhiyong.dailypractice.reactor;

import com.pizhiyong.dailypractice.dao.UserMapper;
import com.pizhiyong.dailypractice.entity.User;
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
public class RectorService {

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
    public void backpressure() {
        Flux<Integer> fastPublisher = Flux.range(1, 1000)
                .doOnRequest(request -> System.out.println("Requested: " + request))
                .doOnNext(c -> System.out.println("Processing: " + c))
                .subscribeOn(Schedulers.parallel());

        fastPublisher.onBackpressureBuffer(10)
                .publishOn(Schedulers.single(), 2)
                .doOnNext(c -> {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("consumed:" + c);
                }).subscribe(
                        item -> {
                        },
                        error -> System.out.println("error:" + error));
    }
}
