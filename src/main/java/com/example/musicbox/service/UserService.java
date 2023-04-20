package com.example.musicbox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.User;

public interface UserService extends IService<User> {
    String login(String username, String password);     // 登录

    long parseToken(String token);   // 解析token

    String register(String username, String password);    // 注册

}
