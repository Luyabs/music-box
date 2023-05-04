package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.entity.Album;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.entity.relation.SongMenuComposition;
import com.example.musicbox.entity.relation.UserMenuCollection;
import com.example.musicbox.mapper.AlbumMapper;
import com.example.musicbox.mapper.SongMenuMapper;
import com.example.musicbox.mapper.relation.SongMenuCompositionMapper;
import com.example.musicbox.mapper.relation.UserMenuCollectionMapper;
import com.example.musicbox.service.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

    @Autowired
    private SongMenuMapper songMenuMapper;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private SongMenuCompositionMapper songMenuCompositionMapper;


    @Override
    public boolean createAlbum(SongMenu songMenu, Album album) {
        songMenu.setCreateTime(null).setUserId(UserInfo.get()).setIsAlbum(true);
        album.setId(songMenu.getId()).setCreateTime(null);
        return songMenuMapper.insert(songMenu) > 0 &&albumMapper.insert(album) > 0;
    }

    @Override
    public boolean changeAlbumInfo(SongMenu newSongMenu, Album Album) {
        return false;
    }

    @Override
    public boolean deleteAlbum(Long songMenuId) {
        return false;
    }
}
