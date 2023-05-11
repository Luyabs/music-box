package com.example.musicbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.dto.PostDto;
import com.example.musicbox.entity.Post;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.entity.relation.SongComment;

public interface PostService extends IService<Post> {

    boolean publishPost(Post newPost);                                  //发表新帖子
    IPage<Post> pagePost(int currentPage, int pageSize, Post condition);//分页获取帖子

    PostDto getPostDtoPage(long post_id);                        //获取帖子及其下回复
    boolean deletePost(long postId);                                    //删除帖子
}
