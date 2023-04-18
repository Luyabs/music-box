package com.example.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.entity.Album;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlbumMapper extends BaseMapper<Album> {
}
