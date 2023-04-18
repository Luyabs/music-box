package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.entity.User;
import com.example.musicbox.mapper.AbstractUserMapper;
import com.example.musicbox.mapper.UserMapper;
import com.example.musicbox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AbstractUserMapper abstractUserMapper;

    @Autowired
    private UserMapper userMapper;
}
