package com.example.musicbox.common;

/**
 * 在解析token后用于存储用户id的ThreadLocal
 */
public class UserInfo {
    private static final ThreadLocal<Integer> userThreadLocal = new ThreadLocal<>();

    public static void set(Integer userId){
        userThreadLocal.set(userId);
    }

    public static Integer get(){
        return userThreadLocal.get();
    }

}
