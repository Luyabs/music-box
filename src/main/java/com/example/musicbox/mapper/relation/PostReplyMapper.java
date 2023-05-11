package com.example.musicbox.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.PostReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostReplyMapper extends BaseMapper<PostReply> {
    @Select("""
            select * from reply_user_post
            where post_id = #{post_id}
            order by create_time DESC
            """)
    List<PostReply> selectPostReplyByPostId(@Param("post_id") long postId);
}
