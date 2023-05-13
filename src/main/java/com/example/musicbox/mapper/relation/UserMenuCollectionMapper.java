package com.example.musicbox.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.entity.relation.UserMenuCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMenuCollectionMapper extends BaseMapper<UserMenuCollection> {

    @Select("""
            select count(*) from collection_user_song_menu
            where user_id = #{user_id} and song_menu_id = #{song_menu_id}
            """)
    int getIfUserCollectMenu(@Param("user_id") long userId,@Param("song_menu_id") long songMenuId);
}
