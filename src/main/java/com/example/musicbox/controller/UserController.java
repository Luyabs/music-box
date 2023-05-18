package com.example.musicbox.controller;

import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.entity.Creator;
import com.example.musicbox.entity.User;
import com.example.musicbox.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@Api("用户信息")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "上传用户头像",notes = "[token]需传入MultipartFile文件流,可以覆盖掉旧的头像")
    @NeedToken
    @PostMapping("/avatar")
    public Result upLoadSongCover(@RequestPart MultipartFile avatarFile){

        boolean res = userService.upLoadUserAvatar(avatarFile, UserInfo.get());
        return res ?Result.success().message("上传（修改）用户头像成功"):Result.error().message("上传（修改）用户头像失败");
    }

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

    @ApiOperation(value = "解析token", notes = "[此方法用于快速校验token, 需要以参数形式传入token]通过token获取用户id")
    @GetMapping("/parse_token")
    public Result info(String token) {
        long userId = userService.parseToken(token);
        return Result.success().data("user_id", userId);
    }

    @ApiOperation(value = "获取全部用户信息")
    @GetMapping("/all")
    public Result getAll() {
        return Result.success().data("list", userService.list());
    }

    @ApiOperation(value = "获取用户自己的信息", notes = "[需要请求头带token]通过token获取用户个人信息")
    @GetMapping("/info")
    @NeedToken
    public Result getUserDetailedInfo() {
        Map<String, Object> info = userService.getUserDetailedInfo();
        return Result.success().data(info);
    }

    @ApiOperation(value = "修改用户信息", notes = "[token] 无效属性id, avatar, status, is_creator, is_vip, create_time, update_time")
    @PutMapping("/info")
    @NeedToken
    public Result changeUserDetailedInfo(@RequestBody User user) {
        boolean res = userService.changeUserDetailedInfo(user);
        return res ? Result.success().message("修改成功") : Result.error().message("修改失败");
    }

    @ApiOperation(value = "修改用户作为创作者的信息", notes = "[token] 无效属性id, creatorIntroduction, stageName, representativeWork, performingExperience, majorAchievement, brokerageCompany")
    @PutMapping("/info/creator")
    @NeedToken
    public Result changeCreatorDetailedInfo(@RequestBody Creator creator) {
        boolean res = userService.changeCreatorDetailedInfo(creator);
        return res ? Result.success().message("修改成功") : Result.error().message("修改失败");
    }

    @ApiOperation(value = "修改用户密码", notes = "[token]")
    @PostMapping("/info/password")
    @NeedToken
    public Result changeUserPassword(String newPassword) {
        boolean res = userService.changeUserPassword(newPassword);
        return res ? Result.success().message("修改密码成功") : Result.error().message("修改密码失败");
    }

    @ApiOperation(value = "升级用户为VIP", notes = "[token]无参数传入")
    @PostMapping("/upgrade/vip")
    @NeedToken
    public Result upgradeToVIP() {
        boolean res = userService.upgradeToVIP();
        return res ? Result.success().message("升级VIP成功") : Result.error().message("升级VIP失败");
    }

    @ApiOperation(value = "升级用户为创作者", notes = "[token]无参数传入")
    @PostMapping("/upgrade/creator")
    @NeedToken
    public Result upgradeToCreator() {
        boolean res = userService.upgradeToCreator();
        return res ? Result.success().message("升级为创作者成功") : Result.error().message("升级为创作者失败");
    }

    @ApiOperation(value = "获取他人的公开信息", notes = "{id}")
    @GetMapping("/visit/{id}")
    public Result getOthersPublicInfo(@PathVariable long id) {
        return Result.success().data(userService.getOthersPublicInfo(id));
    }

    @ApiOperation(value = "管理员获取指定用户信息", notes = "[token] [Admin]")
    @GetMapping("/info/{id}")
    @NeedToken
    public Result getUserInfoAdmin(@PathVariable long id) {
        return Result.success().data(userService.getUserInfoAdmin(id));
    }

    @ApiOperation(value = "管理员修改指定用户状态", notes = "[token] [Admin] 此处无论传入几个参数，只有Status有效")
    @PutMapping("/status")
    @NeedToken
    public Result changeUserStatus(@RequestBody User user) {
        boolean res = userService.changeUserStatus(user);
        return res ? Result.success().message("修改成功") : Result.error().message("修改失败");
    }
}
