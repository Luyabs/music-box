package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.JwtUtils;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.entity.AbstractUser;
import com.example.musicbox.entity.Creator;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.User;
import com.example.musicbox.entity.relation.UserSubscription;
import com.example.musicbox.mapper.AbstractUserMapper;
import com.example.musicbox.mapper.AdministratorMapper;
import com.example.musicbox.mapper.CreatorMapper;
import com.example.musicbox.mapper.UserMapper;
import com.example.musicbox.mapper.relation.UserSubscriptionMapper;
import com.example.musicbox.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AbstractUserMapper abstractUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CreatorMapper creatorMapper;

    @Autowired
    private AdministratorMapper administratorMapper;

    @Value("${file-url.avatar-base-url}")
    private String avatarBaseUrl;   //头像上传地址

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
        if (user != null && user.getUsername() != null)
            map.put("_username", user.getUsername());
        if (userDetail != null)
            map.put("detailed_info", userDetail);
        return map;
    }

    @Override
    public boolean changeUserDetailedInfo(User user) {
        user.setId(UserInfo.get()); // 忽略用户传的id
        user.setAvatar(null).setStatus(null).setIsCreator(null).setIsVip(null).setCreateTime(null);   // 忽略不该更改的属性
        return userMapper.updateById(user) > 0; // 一般不会传false
    }

    @Override
    public boolean changeCreatorDetailedInfo(Creator creator) {
        creator.setId(UserInfo.get());
        User user = userMapper.selectById(creator.getId());
        if (user.getIsCreator()) {  //判断是否为创作者
            creator.setCreateTime(null);   // 忽略不该更改的属性
            return creatorMapper.updateById(creator) > 0;
        } else {
            throw new ServiceException("该用户不是创作者");
        }
    }

    @Override
    public boolean changeUserPassword(String newPassword) {
        AbstractUser abstractUser = abstractUserMapper.selectById(UserInfo.get());
        String oldPassword = abstractUser.getPassword();    //得到用户的旧密码
        if (newPassword == null || newPassword.length() < 6)
            throw new ServiceException("密码需要大于6位");
        if (oldPassword.equals(newPassword))                 //判断新旧密码是否一致
            throw new ServiceException("新旧密码不能一致");
        else {
            abstractUser.setPassword(newPassword);
            return abstractUserMapper.updateById(abstractUser) > 0;
        }
    }

    @Override
    public boolean upgradeToVIP() {
        User user = userMapper.selectById(UserInfo.get());
        Boolean isVIP = user.getIsVip();
        if (isVIP == null)
            throw new ServiceException("isVIP为空");
        if (isVIP)               //判断当前是否已经是VIP
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
        if (isCreator == null)
            throw new ServiceException("isCreator为空");
        if (isCreator)
            throw new ServiceException("当前用户已经是创作者");
        else {
            user.setIsCreator(true);
            int res1 = userMapper.updateById(user);     //更新user表
            Creator newCreator = new Creator().setId(UserInfo.get());//将该用户插入到creator表中
            int res2 = creatorMapper.insert(newCreator);
            return res1 > 0 && res2 > 0;
        }
    }

    @Override
    public Map<String, Object> getOthersPublicInfo(long id) {
        HashMap<String, Object> map = new HashMap<>();
        AbstractUser abstractUser = abstractUserMapper.selectById(id);
        User user = userMapper.selectById(id);
        if (user.getStatus() < 0) {
            throw new ServiceException("用户状态异常");
        }

//        map.put("_user_id",id);    //id是否视作公开信息?
        if (abstractUser != null && abstractUser.getUsername() != null) {
            map.put("_username", abstractUser.getUsername());
        }
        if (user != null) {
            map.put("public_info", user);
        }
        return map;
    }

    @Override
    public Map<String, Object> getUserInfoAdmin(long id) {
        long myId = UserInfo.get();
        if (administratorMapper.selectById(myId) == null) {
            throw new ServiceException("用户无管理员权限");
        }

        HashMap<String, Object> map = new HashMap<>();
        AbstractUser abstractUser = abstractUserMapper.selectById(id);
        User user = userMapper.selectById(id);

        map.put("_user_id", id);
        if (abstractUser != null && abstractUser.getUsername() != null) {
            map.put("_username", abstractUser.getUsername());
        }
        if (user != null) {
            map.put("public_info", user);
        }
        return map;
    }

    @Override
    public boolean changeUserStatus(User user) {
        long myId = UserInfo.get();
        if (administratorMapper.selectById(myId) == null) {
            throw new ServiceException("用户无管理员权限");
        }
        User newUser = new User().setId(user.getId()).setStatus(user.getStatus());  //此处只允许修改用户状态
        return userMapper.updateById(newUser) > 0;
    }

    @Override
    public boolean upLoadUserAvatar(MultipartFile userAvatarFile, Long userID){
        new File(avatarBaseUrl).mkdirs();  // 没有文件夹就创一个
        String originFileName = userAvatarFile.getOriginalFilename();   //原文件名
        String newFileName;      //新文件名
        String prefix;           //后缀名
        File localFile;          //本地文件对象
        int index;
        User user = getUserById(userID);
        if (user.getStatus() != 0)
            throw new ServiceException("用户状态异常（删除/封禁），无法上传头像");
        try {
            if (originFileName != null) {
                index = originFileName.lastIndexOf('.');
                prefix = originFileName.substring(index + 1);
                if (index <= 0 || !prefix.equals("jpg"))
                    throw new ServiceException("用户头像格式错误（只能为jpg）");
            } else {
                throw new ServiceException("文件名错误（不能为空）");
            }
            String randomFileName = UUID.randomUUID().toString();               //服务器本地头像封面随机
            newFileName = avatarBaseUrl + randomFileName + '.' + prefix;             //重构图片名
            localFile = new File(newFileName);
            userAvatarFile.transferTo(localFile);
            user.setAvatar(newFileName);                                        //修改头像
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
        return userMapper.updateById(user) > 0;
    }
    /**
     * 按id获取歌曲 并校验是否为null
     */
    private User getUserById(long userId) {
        User userInfo = userMapper.selectById(userId);
        if (userInfo == null)
            throw new ServiceException("不存在id=" + userId + "的歌曲");
        return userInfo;
    }





}
