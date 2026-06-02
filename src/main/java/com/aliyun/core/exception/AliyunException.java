package com.aliyun.core.exception;

/**
 * 阿里云通用运行时异常。
 */
public class AliyunException extends RuntimeException {

    public AliyunException(String message) {
        super(message);
    }

    public AliyunException(String message, Throwable cause) {
        super(message, cause);
    }
}
