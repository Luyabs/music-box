package com.example.musicbox.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据一致性
 */
@Data
public class Result {
    private boolean success;    // 成功

    private String message;     // 提示信息

    private Map<String, Object> data = new HashMap<>();     // 数据

    private Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static Result success() {
        return new Result(true, "成功");
    }

    //失败静态方法
    public static Result error() {
        return new Result(false, "失败");
    }

    public Result success(boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
