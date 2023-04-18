package com.example.musicbox.controller;

import com.example.musicbox.common.Result;
import com.example.musicbox.entity.AbstractUser;
import com.example.musicbox.entity.User;
import com.example.musicbox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Result getAll() {
        return Result.success().data("list", userService.list());
    }

    @PostMapping("/1")
    public Result hello1(@RequestBody User user) {
        return Result.success().data("user", user);
    }

    @PostMapping("/add")
    public Result add(@RequestBody AbstractUser abstractUser) {
        userService.abstractUserSave(abstractUser);
        return Result.success().message("保存成功");
    }
}
