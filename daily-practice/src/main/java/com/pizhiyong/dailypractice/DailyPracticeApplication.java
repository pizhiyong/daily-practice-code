package com.pizhiyong.dailypractice;

import com.pizhiyong.dailypractice.reactor.ReactorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DailyPracticeApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(DailyPracticeApplication.class, args);
        ReactorService bean = context.getBean(ReactorService.class);
        bean.findUserById(1L).subscribe(c-> System.out.println("根据主键查询：" + c));
        bean.findAllUsers().subscribe(c-> System.out.println("查询所有用户：" + c));
        bean.findUsersOlderThan(24).subscribe(c-> System.out.println("查询年龄大于指定值的用户：" + c));
        bean.backpressure();
    }

}
