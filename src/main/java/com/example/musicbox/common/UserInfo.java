package com.example.musicbox.common;

import com.example.musicbox.common.exception.ServiceException;

/**
 * 在解析token后用于存储用户id的ThreadLocal
 * 在使用UserInfo.get()前需要使用自定义注解@NeedToken来标明需要解析token
 */
public class UserInfo {
    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    public static void set(Long userId){
        userThreadLocal.set(userId);
    }

    public static long get(){
        Long res = userThreadLocal.get();
        if (res == null)
            throw new ServiceException("UserInfo为空, 请尝试在controller方法前增加@NeedToken自定义注解");
        return res;
    }

    public static boolean isNull(){
        return userThreadLocal.get() == null;
    }
}
