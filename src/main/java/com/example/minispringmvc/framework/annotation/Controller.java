package com.example.minispringmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:
 * @date 2024/8/13 10:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Controller {
    String value() default "";
}
