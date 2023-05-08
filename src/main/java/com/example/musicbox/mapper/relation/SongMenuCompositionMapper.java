package com.example.musicbox.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.SongMenuComposition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SongMenuCompositionMapper extends BaseMapper<SongMenuComposition> {
    /**
     * 根据歌单id 查询歌单中所有歌曲
     * @param songMenuId 歌单id
     * @return 歌单列表
     */
    @Select("""
            select s.*
            from composition_song_menu_song c
            join song s on c.song_id = s.id
            where song_menu_id = #{song_menu_id}
            """)
    List<Song> selectSongBySongMenuId(@Param("song_menu_id") long songMenuId);
}
