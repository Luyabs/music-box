package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.dto.AlbumDto;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.mapper.AlbumMapper;
import com.example.musicbox.mapper.SongMenuMapper;
import com.example.musicbox.service.SongMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SongMenuServiceImpl extends ServiceImpl<SongMenuMapper, SongMenu> implements SongMenuService {
    @Autowired
    private SongMenuMapper songMenuMapper;

    @Autowired
    private AlbumMapper albumMapper;

    @Override
    public IPage<SongMenu> getPublicSongMenuPage(int currentPage, int pageSize, SongMenu condition) {
        QueryWrapper<SongMenu> wrapper = new QueryWrapper<SongMenu>()
                .eq("authority", 0)    // 公开
                .eq("is_album", false)  // 非专辑歌单
                .like(condition.getMenuName() != null, "menu_name", condition.getMenuName())    // 歌单名条件
                .like(condition.getMenuIntroduction() != null, "menu_introduction", condition.getMenuIntroduction())   // 歌单简介条件
                .orderByDesc("update_time") ;
        IPage<SongMenu> songMenuPage = songMenuMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);
        return songMenuPage;
    }

    @Override
    public IPage<AlbumDto> getPublicAlbumDtoPage(int currentPage, int pageSize, AlbumDto condition) {
        QueryWrapper<AlbumDto> wrapper = new QueryWrapper<AlbumDto>()
                .eq("authority", 0)    // 公开
                .eq("is_album", true)  // 非专辑歌单
                .like(condition.getMenuName() != null, "menu_name", condition.getMenuName())    // 歌单名条件
                .like(condition.getMenuIntroduction() != null, "album_introduction", condition.getAlbumIntroduction())   // 专辑简介条件
                .orderByDesc("update_time") ;
        IPage<AlbumDto> albumDtoPage = albumMapper.selectPageDto(new Page<>(currentPage, pageSize), wrapper);
        return albumDtoPage;
    }

    @Override
    public SongMenuDto getPublicSongMenuDtoById(long songMenuId) {
        SongMenuDto songMenuDto = songMenuMapper.selectDtoById(songMenuId);
        if (songMenuDto == null)
            throw new ServiceException("不存在song_menu_id=" + songMenuId + "的歌单");
        return songMenuDto;
    }
}
