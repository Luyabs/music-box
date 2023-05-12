package com.example.musicbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.dto.PostDto;
import com.example.musicbox.entity.Post;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.entity.relation.PostReply;
import com.example.musicbox.entity.relation.SongComment;

import java.util.List;

public interface PostService extends IService<Post> {

    boolean publishPost(Post newPost);                                  //发表新帖子
    IPage<Post> pagePost(int currentPage, int pageSize, Post condition);//分页获取帖子

    PostDto getPostDtoPage(long post_id);                        //获取帖子及其下回复
    boolean deletePost(long postId);                                    //删除帖子

    boolean deleteReply(long replyId);                                 //删除帖子下的评论

    Post getPostByReplyId(long replyId);                               //通过评论id// 找到主题帖
    boolean replyPostOrComment(PostReply postReply);                   //回复帖子/评论
    IPage<Post> getMyPost(int currentPage, int pageSize);              //获取自己发布的帖子

    IPage<PostReply> getMyReply(int currentPage, int pageSize);        //获取自己发布的回复
}
