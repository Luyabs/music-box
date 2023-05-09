package com.example.musicbox.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.SongPlayRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
}
