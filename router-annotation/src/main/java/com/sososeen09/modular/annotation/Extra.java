package com.sososeen09.modular.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yunlong on 2018/3/24.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Extra {
    /**
     * 注解成员变量，赋值
     *
     * @return
     */
    String name() default "";
}
