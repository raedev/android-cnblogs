package com.rae.cnblogs.sdk;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 接口选项
 * Created by ChenRui on 2017/5/22 0022 23:30.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface ApiOptions {

    /**
     * 是否忽略登录
     */
    boolean ignoreLogin() default false;

}
