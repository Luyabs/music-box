package com.example.musicbox.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据一致性
 */
@Data
@ApiModel("{success, message, data}")
public class Result {
    @ApiModelProperty("本次请求是否成功")
    private boolean success;

    @ApiModelProperty("提示信息")
    private String message;

    @ApiModelProperty("响应数据")
    private Map<String, Object> data = new HashMap<>();

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
