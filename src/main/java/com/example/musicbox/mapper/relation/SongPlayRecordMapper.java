package com.example.musicbox.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.musicbox.dto.SongDto;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.SongPlayRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SongPlayRecordMapper extends BaseMapper<SongPlayRecord> {
    @Select("""
            select s.*
            from playback_record_user_song p
            join song s on p.song_id = s.id
            where p.status in (0, 1)
            and s.status = 0
            and p.user_id = #{user_id}
            order by p.update_time desc
            """)
    IPage<Song> selectSongPageByUserId(@Param("user_id") long userId, Page<Object> objectPage);

    @Update("""
            update playback_record_user_song
            set status = -1
            where user_id = #{user_id}
            """)
    int logicalDeleteHistory(@Param("user_id") long userId);

    @Results(id = "withSong", value = {
            @Result(
                    column = "play_volume", property = "playVolume", javaType = Long.class
            )}
    )
    @Select("""
            select s.*, count(*) play_volume
            from playback_record_user_song  p
                join song s on p.song_id = s.id
            where p.status = 0
            and p.create_time between date_sub(now(),interval #{days} day) and now()
            and s.status = 0
            group by song_id
            having play_volume >= #{minimum_play_volume}
            order by play_volume desc
            """)
    IPage<SongDto> selectSongToplistByPlayVolume (@Param("days")int days,
                                                  @Param("minimum_play_volume")long minimumPlayVolume,
                                                  Page<Object> objectPage);
}
