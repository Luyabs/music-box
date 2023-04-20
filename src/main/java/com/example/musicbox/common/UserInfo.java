package com.example.musicbox.common;

/**
 * 在解析token后用于存储用户id的ThreadLocal
 */
public class UserInfo {
    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    public static void set(long userId){
        userThreadLocal.set(userId);
    }

    public static long get(){
        return userThreadLocal.get();
    }

}
