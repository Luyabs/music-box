package com.example.musicbox.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.musicbox.dto.AlbumDto;
import com.example.musicbox.entity.Album;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AlbumMapper extends BaseMapper<Album> {
    /**
     * 分页条件查询 歌单Dto
     * @param objectPage 分页
     * @param wrapper 查询条件
     * @return 专辑Dto
     */
    @Select("""
            select s.*, a.album_introduction, a.issue_company, a.issue_time
            from song_menu s
            join album a on s.id = a.id
            ${ew.customSqlSegment}
            """)
    IPage<AlbumDto> selectPageDto(Page<Object> objectPage, @Param(Constants.WRAPPER) QueryWrapper<AlbumDto> wrapper);
}
