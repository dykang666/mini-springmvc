package com.example.minispringmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:
 * @date 2024/8/13 10:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Service {
    String value() default "";
}
