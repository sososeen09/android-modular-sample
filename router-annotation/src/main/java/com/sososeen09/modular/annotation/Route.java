package com.sososeen09.modular.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Route {
    /**
     * 路由结点路径
     *
     * @return
     */
    String path();

    /**
     * 分组，放置单个路由表过大
     *
     * @return
     */
    String group() default "";
}
