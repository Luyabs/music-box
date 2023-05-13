package com.example.musicbox.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.musicbox.dto.AlbumDtowithCollection;
import com.example.musicbox.dto.SongDto;
import com.example.musicbox.dto.SongMenuDtowithCollection;
import com.example.musicbox.entity.relation.UserMenuCollection;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMenuCollectionMapper extends BaseMapper<UserMenuCollection> {

    @Select("""
            select count(*) from collection_user_song_menu
            where user_id = #{user_id} and song_menu_id = #{song_menu_id}
            """)
    int getIfUserCollectMenu(@Param("user_id") long userId,@Param("song_menu_id") long songMenuId);

    @Results(id = "withSong", value = {
            @Result(
                    column = "collection", property = "collection", javaType = Long.class
            )}
    )
    @Select("""
            select s.*, count(*) collection from collection_user_song_menu c
                join song_menu sm on c.song_menu_id = sm.id
                join composition_song_menu_song csms on sm.id = csms.song_menu_id
                join song s on csms.song_id = s.id and s.status = 0
            where c.create_time between date_sub(now(),interval #{days} day) and now()
            group by s.id
            having collection >= #{minimum_collection}
            order by collection desc
            """)
    IPage<SongDto> getSongListByCollection(@Param("days")int days,
                                           @Param("minimum_collection")long minimumCollection,
                                           Page<Object> objectPage);





    @Results(id = "withSongMenu", value = {
            @Result(
                    column = "collection", property = "collectionNumber", javaType = Long.class
            )}
    )
    @Select("""
            select sm.*,count(*) collection
            from collection_user_song_menu c join song_menu sm on c.song_menu_id = sm.id
            where sm.authority = 0 and
                  sm.is_album = false and
                  c.create_time between date_sub(now(),interval #{days} day) and now()
            group by song_menu_id
            having collection >= #{minimum_collection}
            order by collection desc
            """)
    IPage<SongMenuDtowithCollection> getSongMenuListByCollection(@Param("days")int days,
                                                                 @Param("minimum_collection")long minimumCollection,
                                                                 Page<Object> objectPage);
    @Results(id = "withAlbum", value = {
            @Result(
                    column = "collection", property = "collectionNumber", javaType = Long.class
            )}
    )
    @Select("""
            select sm.*,count(*) collection
            from collection_user_song_menu c join song_menu sm on c.song_menu_id = sm.id
            where sm.authority = 0 and
                  sm.is_album = true and
                  c.create_time between date_sub(now(),interval #{days} day) and now()
            group by song_menu_id
            having collection >= #{minimum_collection}
            order by collection desc
            """)
    IPage<SongMenuDtowithCollection> getAlbumListByCollection(@Param("days")int days,
                                                              @Param("minimum_collection")long minimumCollection,
                                                              Page<Object> objectPage);
}
