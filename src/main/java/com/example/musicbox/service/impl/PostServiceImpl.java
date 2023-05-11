package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.dto.PostDto;
import com.example.musicbox.entity.Album;
import com.example.musicbox.entity.Post;
import com.example.musicbox.entity.relation.PostReply;
import com.example.musicbox.entity.relation.SongComment;
import com.example.musicbox.mapper.AlbumMapper;
import com.example.musicbox.mapper.PostMapper;
import com.example.musicbox.mapper.relation.PostReplyMapper;
import com.example.musicbox.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostReplyMapper postReplyMapper;
    @Override
    public boolean publishPost(Post post) {
        String subject = post.getSubject();
        String content = post.getContent();
        if(subject == null||subject.length()<5||subject.length()>50)
            throw new ServiceException("帖子主题大小必须为10及以上50个以下");
        if(content == null|| content.length()<10||content.length()>500)
            throw new ServiceException("帖子内容大小必须为5及以上500个以下");
        Post newPost = new Post().
                setUserId(UserInfo.get()).      //帖子所有者默认为上传者
                setStatus(null).                //帖子状态默认为所有人可见
                setSubject(subject).
                setContent(content).
                setCreateTime(null);            //创建时间无法修改
        return postMapper.insert(newPost) == 1;
    }

    @Override
    public IPage<Post> pagePost(int currentPage, int pageSize, Post condition){
        QueryWrapper<Post> wrapper = new QueryWrapper<Post>().
                ge("status",0).
                eq(condition.getUserId()!=null,"user_id",condition.getUserId()).
                like(condition.getSubject()!=null,"subject",condition.getSubject()).
                like(condition.getContent()!=null,"content",condition.getContent()).
                orderByDesc("create_time");
        return postMapper.selectPage(new Page<>(currentPage,pageSize),wrapper);
    }

    @Override
    public PostDto getPostDtoPage(long post_id){
        Post post = getPostById(post_id);
        if(post.getStatus() != 0)
            throw new ServiceException("当前帖子状态异常，游客无法查看");
        return postMapper.selectPostDtoById(post_id);
    }
    @Transactional
    @Override
    public boolean deletePost(long postId){
        Post post = getPostById(postId);
        if(!post.getUserId().equals(UserInfo.get()))
            throw new ServiceException("无权限删除他人帖子");
        if(post.getStatus()<0)
            throw new ServiceException("帖子状态异常：被删/屏蔽，无法被删除");
        PostReply newPostReply = new PostReply().
                setId(null).
                setPostId(null).
                setStatus(-1).
                setCreateTime(null);
        QueryWrapper<PostReply> wrapper = new QueryWrapper<PostReply>().
                eq("post_id",postId).
                ge("status",0);
        post.setStatus(-1);
        return postReplyMapper.update(newPostReply,wrapper)>=0&&
                postMapper.updateById(post) == 1;
    }

    private Post getPostById(long postId) {
        Post postInfo = postMapper.selectById(postId);
        if (postInfo == null)
            throw new ServiceException("不存在id=" + postId + "的帖子");
        return postInfo;
    }
}
