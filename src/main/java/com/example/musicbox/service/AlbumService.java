package com.example.musicbox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.Album;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.SongMenu;
import org.springframework.stereotype.Service;


public interface AlbumService extends IService<Album> {
    boolean createAlbum(SongMenu newSongMenu ,Album Album);         //创建专辑
    boolean changeAlbumInfo(SongMenu newsongMenu ,Album Album);     //修改专辑信息
    boolean deleteAlbum(Long songMenuId);                           //删除专辑
}
