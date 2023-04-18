package com.example.musicbox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.AbstractUser;
import com.example.musicbox.entity.User;

public interface UserService extends IService<User> {
    int abstractUserSave(AbstractUser abstractUser);
}
