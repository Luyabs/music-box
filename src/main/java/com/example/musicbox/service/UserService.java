package com.example.musicbox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    String login(String username, String password);     // 登录

    String register(String username, String password);    // 注册

    long parseToken(String token);    // 解析token [非请求头形式传token]

    Map<String, Object> getUserDetailedInfo();   // 根据token 获取用户详细信息

    boolean changeUserDetailedInfo(User user);   // 修改用户信息

    boolean changeUserPassword(String newPassword); // 修改用户密码

    boolean upgradeToVIP(); //升级为VIP用户

    boolean upgradeToCreator();//升级为创作者
}
