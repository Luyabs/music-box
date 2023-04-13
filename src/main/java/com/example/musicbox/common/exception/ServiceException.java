package com.example.musicbox.common.exception;

/**
 * 自定义业务层异常类
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message){
        super(message);
    }
}
