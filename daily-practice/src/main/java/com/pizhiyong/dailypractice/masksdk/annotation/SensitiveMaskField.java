package com.pizhiyong.dailypractice.masksdk.annotation;

import com.pizhiyong.dailypractice.masksdk.enums.SensitiveMaskHandleType;

import java.lang.annotation.*;

/**
 * 针对需要脱敏的字段添加该注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface SensitiveMaskField {

    SensitiveMaskHandleType type() default SensitiveMaskHandleType.MASK_ALL;

}
