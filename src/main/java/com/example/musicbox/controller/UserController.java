package com.example.musicbox.controller;

import com.example.musicbox.common.Result;
import com.example.musicbox.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Api("用户信息")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录", notes = "使用用户名 + 密码进行登录")
    @PostMapping("/login")
    public Result login(String username, String password) {
        String token = userService.login(username, password);
        return Result.success().data("token", token);
    }


    @ApiOperation(value = "登出", notes = "这个接口在后端什么也不会干")
    @PostMapping("/logout")
    public Result logout() {
        return Result.success().message("登出成功");
    }

    @ApiOperation(value = "注册", notes = "使用用户名 + 密码进行注册")
    @PostMapping("/register")
    public Result register(String username, String password) {
        String token = userService.register(username, password);
        return Result.success().data("token", token);
    }

    @ApiOperation("获取全部用户数据")
    @GetMapping("/all")
    public Result getAll() {
        return Result.success().data("list", userService.list());
    }

    @ApiOperation(value = "解析token", notes = "[此方法用于快速校验token, 需要以参数形式传入token]通过token获取用户个人信息")
    @GetMapping("/info")
    public Result info(String token) {
        long userId = userService.parseToken(token);
        return Result.success().data("user_id", userId);
    }

    @ApiOperation(value = "获取用户详细信息", notes = "[需要请求头带token]通过token获取用户个人信息")
    @GetMapping("/detailed_info")
    public Result getUserDetailedInfo() {
        Map<String, Object> info = userService.getUserDetailedInfo();
        return Result.success().data(info);
    }
}
