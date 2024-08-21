package com.example.minispringmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:
 * @date 2024/8/13 10:54
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
    boolean required() default true;
}
