package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.entity.User;
import com.example.musicbox.entity.relation.UserChat;
import com.example.musicbox.entity.relation.UserSubscription;
import com.example.musicbox.mapper.UserMapper;
import com.example.musicbox.mapper.relation.UserChatMapper;
import com.example.musicbox.mapper.relation.UserSubscriptionMapper;
import com.example.musicbox.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialServiceImpl extends ServiceImpl<UserMapper, User> implements SocialService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;
    @Autowired
    private UserChatMapper userChatMapper;

    @Override
    public boolean follow(long userId) {
        long myId = UserInfo.get();
        UserSubscription userSubscription = new UserSubscription();
        if (userMapper.selectById(userId) == null) {
            throw new ServiceException("该被关注用户不存在");
        }
        if (userSubscriptionMapper.selectOne(new QueryWrapper<UserSubscription>().eq("follower_id", myId).eq("followed_id", userId)) != null) {
            throw new ServiceException("不允许重复关注");
        }
        int res = userSubscriptionMapper.insert(userSubscription.setFollowerId(myId).setFollowedId(userId));
        return res > 0;
    }

    @Override
    public IPage<User> getUserFollowedPage(int pageNumber, int pageSize) {
        long userId = UserInfo.get();
        IPage<User> page = userSubscriptionMapper.selectUserFollowed(userId, new Page<User>(pageNumber, pageSize));
        return page;
    }

    @Override
    public IPage<User> getUserFollowerPage(int pageNumber, int pageSize) {
        long userId = UserInfo.get();
        IPage<User> page = userSubscriptionMapper.selectUserFollower(userId, new Page<User>(pageNumber, pageSize));
        return page;
    }

    @Override
    public boolean chat(UserChat userChat) {
        userChat.setSenderId(UserInfo.get()).setId(null).setStatus(null);   //忽略无效属性id、状态属性
        if (userChat.getReceiverId().equals(userChat.getSenderId())) {
            throw new ServiceException("不允许自己给自己发私信");
        }
        if (userMapper.selectById(userChat.getReceiverId()) == null) {
            throw new ServiceException("接收用户不存在");
        }
        return userChatMapper.insert(userChat) > 0;
    }

    @Override
    public Page<UserChat> getUserChatRecords(int currentPage, int pageSize, long userId) {
        long myId = UserInfo.get();
        QueryWrapper<UserChat> wrapper = new QueryWrapper<UserChat>()
                .and(w -> {
                    w.eq("receiver_id", userId)
                            .eq("sender_id", myId)
                            .or()
                            .eq("sender_id", userId)
                            .eq("receiver_id", myId);
                })
                .orderByDesc("update_time")
                .eq("status", 0);
        Page<UserChat> userChatPage = userChatMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);
        return userChatPage;
    }

    @Override
    public boolean deleteChat(long chatId) {
        long userId = UserInfo.get();
        UserChat chat = userChatMapper.selectById(chatId);
        if (chat == null) {
            throw new ServiceException("不存在id=" + chatId + "的聊天");
        }
        if (userId != chat.getSenderId()) {
            throw new ServiceException("当前用户无权删除他人私信");
        }
        if (chat.getStatus() < 0) {
            throw new ServiceException("该聊天记录状态异常（删除/屏蔽）");
        }
        chat.setStatus(-1);
        return userChatMapper.updateById(chat) > 0;
    }
}
