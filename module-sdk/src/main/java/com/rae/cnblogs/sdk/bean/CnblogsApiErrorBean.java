package com.rae.cnblogs.sdk.bean;

/**
 * 博客园官方接口发生错误时候的实体信息
 * Created by ChenRui on 2017/1/14 14:43.
 */
public class CnblogsApiErrorBean {

    private String error;
    private String Message;
    private String ExceptionMessage;
    private String ExceptionType;
    private String StackTrace;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getExceptionMessage() {
        return ExceptionMessage;
    }

    public void setExceptionMessage(String ExceptionMessage) {
        this.ExceptionMessage = ExceptionMessage;
    }

    public String getExceptionType() {
        return ExceptionType;
    }

    public void setExceptionType(String ExceptionType) {
        this.ExceptionType = ExceptionType;
    }

    public String getStackTrace() {
        return StackTrace;
    }

    public void setStackTrace(String StackTrace) {
        this.StackTrace = StackTrace;
    }
}
