package com.example.musicbox.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.entity.relation.SongComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SongCommentMapper extends BaseMapper<SongComment> {
}
