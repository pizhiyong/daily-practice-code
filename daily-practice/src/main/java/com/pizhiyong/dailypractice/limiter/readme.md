# 限流算法

2、Guava + aop实现接口限流
* 定义限流注解
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
* 定义切面
  @Aspect
  @Component
  public class RateLimitAspect {

  private static final RateLimiter limiter = RateLimiter.create(Double.MAX_VALUE);

  /**
    * 设置切点
      */
      @Pointcut("@annotation(com.pzy.ceshi.currentLimiting.annotation.CustomerRateLimit)")
      public void pointCut() {

  }

  @Around(value = "pointCut()")
  public void handle(ProceedingJoinPoint joinPoint) throws Throwable {
  if (null == joinPoint) {
  return ;
  }
  // 获取注解
  CustomerRateLimit customerRateLimit = getCustomerRateLimit(joinPoint);
  if (customerRateLimit == null) {
  return ;
  }
  double permits = customerRateLimit.permits();
  limiter.setRate(permits);
  if (!limiter.tryAcquire(customerRateLimit.timeout(), customerRateLimit.timeUnit())) {
  System.out.println("服务器繁忙，请稍后重试！！");
  }
  System.out.println(joinPoint.proceed());
  }

  private CustomerRateLimit getCustomerRateLimit(JoinPoint joinPoint) throws NoSuchMethodException {
  Object target = joinPoint.getTarget();
  MethodSignature signature = (MethodSignature)joinPoint.getSignature();
  Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
  Method method = target.getClass().getMethod(signature.getMethod().getName(), parameterTypes);
  if (method == null) {
  return null;
  }
  return method.getAnnotation(CustomerRateLimit.class);
  }
  }
* 测试
  @Controller
  @RequestMapping(value = "/rate/limit")
  public class RateLimitController {

  @RequestMapping(value = "/test", method = RequestMethod.GET)
  @CustomerRateLimit(value = "测试限流", permits = 0.1)
  public String testRateLimit() {
  return "test";
  }
  }