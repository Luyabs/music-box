package com.example.musicbox.controller;

import com.example.musicbox.entity.*;
import com.example.musicbox.entity.relation.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelShowController {
    @ApiOperation("仅用于激活Entity包下实体的注解")
    @PostMapping("_____hello")
    public String showModel(
            @RequestPart(required = false) AbstractUser abstractUser,
            @RequestPart(required = false) Administrator administrator,
            @RequestPart(required = false) Album album,
            @RequestPart(required = false) Creator creator,
            @RequestPart(required = false) Post post,
            @RequestPart(required = false) Song song,
            @RequestPart(required = false) SongMenu songMenu,
            @RequestPart(required = false) User user,
            @RequestPart(required = false) PostReply postReply,
            @RequestPart(required = false) SongComment songComment,
            @RequestPart(required = false) SongMenuComposition songMenuComposition,
            @RequestPart(required = false) SongPlayRecord songPlayRecord,
            @RequestPart(required = false) UserChat userChat,
            @RequestPart(required = false) UserMenuCollection userMenuCollection,
            @RequestPart(required = false) UserSubscription userSubscription
            ) {
        return "hello";
    }
}
