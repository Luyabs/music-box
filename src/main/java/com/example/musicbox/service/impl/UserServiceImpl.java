package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.JwtUtils;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.entity.AbstractUser;
import com.example.musicbox.entity.User;
import com.example.musicbox.mapper.AbstractUserMapper;
import com.example.musicbox.mapper.UserMapper;
import com.example.musicbox.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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

        return JwtUtils.generateToken(targetUser.getId().toString());   // 把用户id生成token
    }

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
        try {
            Map<String, Object> map = new HashMap<>();
            long userId = UserInfo.get();   // 通过token获取用户id
            AbstractUser user = abstractUserMapper.selectById(userId);
            User userDetail = userMapper.selectById(userId);
            map.put("_user_id", userId);
            map.put("_username", user.getUsername());
            map.put("detailed_info", userDetail);
            return map;
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
    @Override
    public Boolean changeUserDetailedInfo(User user){
        try {

            if(!user.getId().equals(UserInfo.get()))//确认用户id
                return false;
            else if(user.getNickname().length()>80)
                return false;
            else if(user.getAvatar().length()>120)
                return false;
            else if(user.getGender().length()>5)
                return false;
            else if (user.getRegion().length()>50)
                return false;
            else if(user.getSignature().length()>80)
                return false;
            else if(user.getProfession().length()>80)
                return false;
            /////**有待修改li**/////
            int userId = userMapper.updateById(user);
            return true;
        }catch(Exception ex){
            throw new ServiceException(ex.getMessage());
        }

    }
}
