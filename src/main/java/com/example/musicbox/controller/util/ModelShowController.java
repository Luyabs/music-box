package com.example.musicbox.controller.util;

import com.example.musicbox.entity.*;
import com.example.musicbox.entity.relation.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelShowController {
    @ApiOperation("仅用于激活Entity包下实体的注解")
    @PostMapping("_____hello")
    public String showModel(
            @RequestBody(required = false) AbstractUser abstractUser,
            @RequestBody(required = false) Administrator administrator,
            @RequestBody(required = false) Album album,
            @RequestBody(required = false) Creator creator,
            @RequestBody(required = false) Post post,
            @RequestBody(required = false) Song song,
            @RequestBody(required = false) SongMenu songMenu,
            @RequestBody(required = false) User user,
            @RequestBody(required = false) PostReply postReply,
            @RequestBody(required = false) SongComment songComment,
            @RequestBody(required = false) SongMenuComposition songMenuComposition,
            @RequestBody(required = false) SongPlayRecord songPlayRecord,
            @RequestBody(required = false) UserChat userChat,
            @RequestBody(required = false) UserMenuCollection userMenuCollection,
            @RequestBody(required = false) UserSubscription userSubscription
            ) {
        return "hello";
    }
}
