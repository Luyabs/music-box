package com.example.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
