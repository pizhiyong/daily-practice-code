package com.pizhiyong.dailypractice.limiter.guava;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Target(value = {ElementType.METHOD, ElementType.PARAMETER})
public @interface CustomizeRateLimit {

    /**
     * 自定义名称
     *
     * @return
     */
    String value() default "";

    /**
     * 每秒产生的令牌数，默认为double最大值
     *
     * @return
     */
    double permits() default Double.MAX_VALUE;

    /**
     * 获取令牌的超时时间，默认为0
     *
     * @return
     */
    int timeout() default 0;

    /**
     * 超时时间单位，默认为毫秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
