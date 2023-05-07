package com.example.musicbox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.dto.AlbumDto;
import com.example.musicbox.entity.Album;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.SongMenu;
import org.springframework.stereotype.Service;


public interface AlbumService extends IService<Album> {
    boolean createAlbum(AlbumDto albumDto);         //创建专辑
    boolean changeAlbumInfo(AlbumDto albumDto);     //修改专辑信息
    boolean deleteAlbum(long songMenuId);           //删除专辑
}
