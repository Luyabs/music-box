package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.dto.PostDto;
import com.example.musicbox.entity.Post;
import com.example.musicbox.entity.Song;
import com.example.musicbox.mapper.PostMapper;
import com.example.musicbox.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("帖子")
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;

    @ApiOperation(value = "发表帖子[限登录状态]", notes = "[token] 只有帖子主题、内容有效，用户id默认为当前登录账号，其他属性无效")
    @NeedToken
    @PostMapping("/topic")
    public Result publishPost(@RequestBody Post newPost){
        boolean res = postService.publishPost(newPost);
        return res ? Result.success().message("发布帖子成功") : Result.error().message("发布帖子失败");
    }
    @ApiOperation(value = "分页获取帖子", notes = "支持通过user_id/content/subject三种方式查询")
    @GetMapping("/topic")
    public Result getPost(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, Post condition){
        IPage<Post> postPage = postService.pagePost(currentPage, pageSize, condition);
        return  Result.success().data("post_page", postPage);
    }

    @ApiOperation(value = "按id获取主题帖（及其下回复）",notes = "返回值为postDto")
    @GetMapping ("/topic/{post_id}")
    public Result getPostDto(@PathVariable long post_id){
        PostDto postDtoPage = postService.getPostDtoPage(post_id);
        return  Result.success().data("postDTO", postDtoPage);
    }
    @ApiOperation(value = "删除帖子", notes = "[token] 帖子状态设为-1")
    @NeedToken
    @DeleteMapping ("/topic/{post_id}")
    public Result deletePost(@PathVariable long post_id){
        boolean res = postService.deletePost(post_id);
        return res ? Result.success().message("删除帖子成功") : Result.error().message("删除帖子失败");    }

}
