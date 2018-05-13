package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.parser.IHtmlParser;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 解析器
 * Created by ChenRui on 2017/5/22 0022 23:30.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Parser {
    /**
     * HTML解析器
     */
    Class<? extends IHtmlParser> value();

}
