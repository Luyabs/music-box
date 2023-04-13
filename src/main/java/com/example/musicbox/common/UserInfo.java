package com.example.musicbox.common;

/**
 * 在解析token后用于存储用户id的ThreadLocal
 */
public class UserInfo {
    private static final ThreadLocal<Integer> userThreadLocal = new ThreadLocal<>();

    public static void set(int userId){
        userThreadLocal.set(userId);
    }

    public static int get(){
        return userThreadLocal.get();
    }

}
