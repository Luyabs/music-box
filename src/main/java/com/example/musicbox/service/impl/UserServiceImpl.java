package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.JwtUtils;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.entity.AbstractUser;
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

    @Override
    public String login(String username, String password) {
        AbstractUser targetUser = abstractUserMapper.selectOne(     // 查询目标
                new QueryWrapper<AbstractUser>()
                        .eq("username", username)
                        .eq("password", password)
        );
        if (targetUser == null)
            throw new ServiceException("用户名或密码错误");

        User targetUserDetail = userMapper.selectById(targetUser.getId());      // 查询目标详细信息
        if (targetUserDetail.getStatus() == 1)      // 状态:1 代表封禁状态
            throw new ServiceException("用户处于封禁状态");

        String token = JwtUtils.generateToken(targetUser.getId().toString());// 把用户id生成token
        return token;
    }

    @Override
    public int parseToken(String token) {
        try {
            return Integer.parseInt(JwtUtils.decodeByToken(token));
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public int abstractUserSave(AbstractUser abstractUser) {
        return abstractUserMapper.insert(abstractUser);
    }
}
