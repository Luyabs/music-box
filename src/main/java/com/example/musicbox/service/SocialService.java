package com.example.musicbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.User;
import com.example.musicbox.entity.relation.UserChat;

public interface SocialService extends IService<User> {
    boolean follow(long UserId);  //关注其他用户

    IPage<User> getUserFollowedPage(int pageNumber,int pageSize);   //查询用户关注列表

    IPage<User> getUserFollowerPage(int pageNumber,int pageSize);   //查询用户粉丝列表

    boolean chat(UserChat userChat);   //发私信

    Page<UserChat> getUserChatRecords(int currentPage, int pageSize, long userId);   //查询和指定用户的聊天记录

    boolean deleteChat(long chatId);
}
