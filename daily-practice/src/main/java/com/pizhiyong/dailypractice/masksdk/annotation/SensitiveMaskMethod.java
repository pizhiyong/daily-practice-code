package com.pizhiyong.dailypractice.masksdk.annotation;

import java.lang.annotation.*;

/**
 * 针对需要脱敏的方法，添加此注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SensitiveMaskMethod {

}
