package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.parser.IJsonParser;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JSON解析器
 * Created by ChenRui on 2017/6/7 0007 19:18.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface JsonParser {

    /**
     * JSON解析器
     */
    Class<? extends IJsonParser> value();
}
