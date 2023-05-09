package com.example.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.SongMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SongMenuMapper extends BaseMapper<SongMenu> {
    /**
     * 按id查询 [公开的]歌单Dto
     * @param songMenuId 歌单id
     * @return 歌单Dto
     * ResultMap: 按歌单id获取歌单中所有歌曲
     */
    @Results(id = "withSong", value = {
            @Result(
                    column = "id4mapping", property = "songList", javaType = List.class,
                    many = @Many(select = "com.example.musicbox.mapper.relation.SongMenuCompositionMapper.selectSongBySongMenuId")
            )}
    )
    @Select("""
            select *, id id4mapping
            from song_menu
            where id = #{song_menu_id}
            and authority = 0
            """)
    SongMenuDto selectPublicDtoById(@Param("song_menu_id") long songMenuId);

    /**
     * 按id查询 [自己的]歌单Dto
     * @param songMenuId 歌单id
     * @param userId 用户id
     * @return 歌单Dto
     */
    @ResultMap("withSong")
    @Select("""
            select *, id id4mapping
            from song_menu
            where id = #{song_menu_id}
            and user_id = #{user_id}
            and authority != -1
            """)
    SongMenuDto selectMyDtoById(@Param("song_menu_id") long songMenuId, @Param("user_id") long userId);
}
