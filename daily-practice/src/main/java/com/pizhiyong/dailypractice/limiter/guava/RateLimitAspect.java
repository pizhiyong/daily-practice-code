package com.pizhiyong.dailypractice.limiter.guava;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.RateLimiter;

@Aspect
@Component
public class RateLimitAspect {

    private static final RateLimiter limiter = RateLimiter.create(Double.MAX_VALUE);

    /**
     * 设置切点
     */
    @Pointcut("@annotation(com.pizhiyong.dailypractice.limiter.guava.CustomizeRateLimit)")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public void handle(ProceedingJoinPoint joinPoint) throws Throwable {
        if (null == joinPoint) {
            return;
        }
        // 获取注解
        CustomizeRateLimit customerRateLimit = getCustomerRateLimit(joinPoint);
        if (customerRateLimit == null) {
            return;
        }
        double permits = customerRateLimit.permits();
        limiter.setRate(permits);
        if (!limiter.tryAcquire(customerRateLimit.timeout(), customerRateLimit.timeUnit())) {
            System.out.println("服务器繁忙，请稍后重试！！");
        }
        System.out.println(joinPoint.proceed());
    }

    private CustomizeRateLimit getCustomerRateLimit(JoinPoint joinPoint) throws NoSuchMethodException {
        Object target = joinPoint.getTarget();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Method method = target.getClass().getMethod(signature.getMethod().getName(), parameterTypes);
        return method.getAnnotation(CustomizeRateLimit.class);
    }

    public static void main(String[] args) {
        System.out.println("123");
    }

}
