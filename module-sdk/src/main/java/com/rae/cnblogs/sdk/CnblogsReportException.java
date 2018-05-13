package com.rae.cnblogs.sdk;

/**
 * 上报的异常
 * Created by ChenRui on 2017/9/7 0007 19:05.
 */
public class CnblogsReportException extends RuntimeException {

    public CnblogsReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
