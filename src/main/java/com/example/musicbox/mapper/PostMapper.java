package com.example.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.dto.PostDto;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 按id查询 [公开的]帖子Dto
     * @param postId 帖子id
     * @return 帖子Dto
     * ResultMap: 按帖子id获取帖子中所有回复
     */
    @Results(id = "withPost" ,value = {
            @Result(
                    column = "id4mapping",property = "postReplyList",javaType = List.class,
                    many = @Many(select = "com.example.musicbox.mapper.relation.PostReplyMapper.selectPostReplyByPostId")
            )}
    )
    @Select("""
            select *,id id4mapping
            from post
            where id = #{postId}
            and status = 0
            """)
    PostDto selectPostDtoById(@Param("postId") long postId);
}
