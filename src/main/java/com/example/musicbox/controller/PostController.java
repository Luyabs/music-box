package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.dto.PostDto;
import com.example.musicbox.entity.Post;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.PostReply;
import com.example.musicbox.mapper.PostMapper;
import com.example.musicbox.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return  Result.success().message("获取帖子成功").data("postDTO", postDtoPage);
    }
    @ApiOperation(value = "删除帖子", notes = "[token] 帖子状态设为-1")
    @NeedToken
    @DeleteMapping ("/topic/{post_id}")
    public Result deletePost(@PathVariable long post_id){
        boolean res = postService.deletePost(post_id);
        return res ? Result.success().message("删除帖子成功") : Result.error().message("删除帖子失败");
    }

    @ApiOperation(value = "回复主题帖 + 回复别人的回复",notes = "[token]" +
            "只有post_id和comments_cotent有效；回复帖子reply_id置空，回复评论reply_id设位回复的评论id")
    @NeedToken
    @PostMapping("/reply")
    public Result replyPostOrComment(@RequestBody PostReply postReply){
        boolean res = postService.replyPostOrComment(postReply);
        return res ? Result.success().message("回复成功") : Result.error().message("回复失败");
    }
    @ApiOperation(value = "删自己的评论 + 作为楼主删别人的评论",notes = "[token]只需要传回复id；回复被删评论的评论不会被删")
    @NeedToken
    @DeleteMapping("/reply/{reply_id}")
    public Result deleteReply(@PathVariable long reply_id){
        boolean res = postService.deleteReply(reply_id);
        return res ? Result.success().message("删除回复成功") : Result.error().message("删除回复失败");
    }
    @ApiOperation(value = "通过评论找到主题帖",notes = "[token]只需要传回复id")
    @NeedToken
    @GetMapping ("/reply/{reply_id}")
    public Result getPostByReplyId(@PathVariable long reply_id){
        Post post = postService.getPostByReplyId(reply_id);
        return Result.success().message("根据评论id查找帖子成功").data("poet",post);
    }
    @ApiOperation(value = "查询自己发表过的主题帖" ,notes = "[token]")
    @NeedToken
    @GetMapping("/my/topic")
    public Result getMyPost(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10")int pageSize){
        IPage<Post> postPage = postService.getMyPost(currentPage, pageSize);
        return Result.success().message("查看自己发布的帖子成功").data("myPost",postPage);
    }
    @ApiOperation(value = "查询自己发表过的回复/评论" ,notes = "[token]")
    @NeedToken
    @GetMapping("/my/reply")
    public Result getMyReply(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10")int pageSize){
        IPage<PostReply> postReplyIPage = postService.getMyReply(currentPage, pageSize);
        return Result.success().message("查看自己发布的帖子成功").data("myPost",postReplyIPage);
    }


}
