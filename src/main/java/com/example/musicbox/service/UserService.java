package com.example.musicbox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    String login(String username, String password);     // 登录

    String register(String username, String password);    // 注册

    long parseToken(String token);    // 解析token [非请求头形式传token]

    Map<String, Object> getUserDetailedInfo();   // 根据token 获取用户详细信息


}
