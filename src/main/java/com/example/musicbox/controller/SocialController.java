package com.example.musicbox.controller;

import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.entity.relation.UserChat;
import com.example.musicbox.service.SocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("社交")
@RequestMapping("/social")
public class SocialController {
    @Autowired
    private SocialService socialService;

    @ApiOperation(value = "关注", notes = "[token] user_id")
    @PostMapping("/follow/{user_id}")
    @NeedToken
    public Result follow(@PathVariable long user_id) {
        boolean res = socialService.follow(user_id);
        return res ? Result.success().message("关注成功") : Result.error().message("关注失败");
    }

    @ApiOperation(value = "查询自己的关注列表", notes = "[token] currentPage, pageSize")
    @GetMapping("/follow")
    @NeedToken
    public Result userFollowedPage(@RequestParam(defaultValue = "1") int currentPage,
                            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success().data("pages", socialService.getUserFollowedPage(currentPage, pageSize));
    }

    @ApiOperation(value = "查询自己的粉丝列表", notes = "[token] currentPage, pageSize")
    @GetMapping("/fan")
    @NeedToken
    public Result userFollowerPage(@RequestParam(defaultValue = "1") int currentPage,
                            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success().data("pages", socialService.getUserFollowerPage(currentPage, pageSize));
    }

    @ApiOperation(value = "发送私信", notes = "[token] userChat")
    @PostMapping("/message")
    @NeedToken
    public Result chat(@RequestBody UserChat userChat) {
        boolean res = socialService.chat(userChat);
        return res ? Result.success().message("发送私信成功") : Result.error().message("发送私信失败");
    }

    @ApiOperation(value = "查询自己对指定用户的聊天记录", notes = "[token] {user_id}")
    @GetMapping("/message/{user_id}")
    @NeedToken
    public Result getUserChatRecords(@RequestParam(defaultValue = "1") int currentPage,
                                     @RequestParam(defaultValue = "10") int pageSize,
                                     @PathVariable long user_id) {
        return Result.success().data("page", socialService.getUserChatRecords(currentPage, pageSize, user_id));
    }

    @ApiOperation(value = "删除指定的聊天记录", notes = "[token] {chat_id}")
    @DeleteMapping("/message/{chat_id}")
    @NeedToken
    public Result deleteChat(@PathVariable long chat_id) {
        boolean res = socialService.deleteChat(chat_id);
        return res ? Result.success().message("删除成功") : Result.error().message("删除失败");
    }

}
