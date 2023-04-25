package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.JwtUtils;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.entity.AbstractUser;
import com.example.musicbox.entity.Creator;
import com.example.musicbox.entity.User;
import com.example.musicbox.mapper.AbstractUserMapper;
import com.example.musicbox.mapper.CreatorMapper;
import com.example.musicbox.mapper.UserMapper;
import com.example.musicbox.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AbstractUserMapper abstractUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CreatorMapper creatorMapper;

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

        return JwtUtils.generateToken(targetUser.getId().toString());   // 把用户id生成token
    }

    @Transactional
    @Override
    public String register(String username, String password) {
        if (username.equals(""))
            throw new ServiceException("用户名不能为空");
        if (abstractUserMapper.exists(new QueryWrapper<AbstractUser>().eq("username", username)))
            throw new ServiceException("用户名已存在");
        if (password.length() < 6)
            throw new ServiceException("密码需要大于6位");

        AbstractUser registerUser = new AbstractUser().setUsername(username).setPassword(password);
        abstractUserMapper.insert(registerUser);
        userMapper.insert(new User().setId(registerUser.getId()));

        return JwtUtils.generateToken(registerUser.getId().toString());     // 把用户id生成token
    }

    @Override
    public long parseToken(String token) {
        try {
            return Long.parseLong(JwtUtils.decodeByToken(token));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public Map<String, Object> getUserDetailedInfo() {
        Map<String, Object> map = new HashMap<>();
        long userId = UserInfo.get();   // 通过token获取用户id
        AbstractUser user = abstractUserMapper.selectById(userId);
        User userDetail = userMapper.selectById(userId);
        map.put("_user_id", userId);
        map.put("_username", user.getUsername());
        map.put("detailed_info", userDetail);
        return map;
    }

    @Override
    public boolean changeUserDetailedInfo(User user){
        user.setId(UserInfo.get()); // 忽略用户传的id
        user.setAvatar(null).setStatus(null).setIsCreator(null).setIsVip(null).setCreateTime(null);   // 忽略不该更改的属性
        return userMapper.updateById(user) > 0; // 一般不会传false
    }

    @Override
    public boolean changeUserPassword(String newPassword) {
        AbstractUser abstractUser = abstractUserMapper.selectById(UserInfo.get());
        String oldPassword = abstractUser.getPassword();    //得到用户的旧密码
        if (newPassword == null ||newPassword.length() < 6)
            throw new ServiceException("密码需要大于6位");
        if(oldPassword.equals(newPassword))                 //判断新旧密码是否一致
            throw new ServiceException("新旧密码不能一致");
        else{
            abstractUser.setPassword(newPassword);
            return abstractUserMapper.updateById(abstractUser) > 0;
        }
    }

    @Override
    public boolean upgradeToVIP() {
        User user = userMapper.selectById(UserInfo.get());
        if(user.getIsVip())               //判断当前是否已经是VIP
            throw new ServiceException("当前用户已经是VIP");
        else
            user.setIsVip(true);
        return userMapper.updateById(user) > 0;
    }

    @Transactional
    @Override
    public boolean upgradeToCreator() {
        User user = userMapper.selectById(UserInfo.get());
        Boolean isCreator = user.getIsCreator();
        if(isCreator)
            throw new ServiceException("当前用户已经是创作者");
        else{
            user.setIsCreator(true);
            int res1 = userMapper.updateById(user);     //更新user表
            Creator newCreator = new Creator().setId(UserInfo.get());//将该用户插入到creator表中
            int res2 = creatorMapper.insert(newCreator);
            return res1 > 0 && res2 > 0;
        }
    }
}
