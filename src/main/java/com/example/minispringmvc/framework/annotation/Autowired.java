package com.example.minispringmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:
 * @date 2024/8/13 10:48
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
