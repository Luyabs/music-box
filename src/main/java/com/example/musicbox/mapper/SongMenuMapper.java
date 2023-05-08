package com.example.musicbox.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.SongMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SongMenuMapper extends BaseMapper<SongMenu> {
    /**
     * 分页条件查询 歌单Dto
     * @param objectPage 分页
     * @param queryWrapper 查询条件
     * @return 歌单Dto
     */
    @Select("""
            select *
            from song_menu
            ${ew.customSqlSegment}
            """)
    IPage<SongMenuDto> selectPageDto(Page<Object> objectPage, @Param(Constants.WRAPPER) QueryWrapper<SongMenuDto> queryWrapper);


    /**
     * 按id查询 歌单Dto
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
            where id = #{id}
            and authority = 0
            """)
    SongMenuDto selectDtoById(@Param("id") long songMenuId);
}
