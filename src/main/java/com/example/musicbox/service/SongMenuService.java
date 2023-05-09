package com.example.musicbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.dto.AlbumDto;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.SongMenu;

public interface SongMenuService extends IService<SongMenu> {

    IPage<SongMenu> getPublicSongMenuPage(int currentPage, int pageSize, SongMenu condition);

    IPage<AlbumDto> getPublicAlbumDtoPage(int currentPage, int pageSize, AlbumDto condition);

    SongMenuDto getPublicSongMenuDtoById(long songMenuId);

    IPage<SongMenu> getMySongMenuPage(int currentPage, int pageSize, SongMenu condition);

    IPage<AlbumDto> getMyAlbumDtoPage(int currentPage, int pageSize, AlbumDto condition);

    SongMenuDto getMySongMenuDtoById(long songMenuId);

    boolean saveSongMenu(SongMenu songMenu);

    boolean editSongMenu(SongMenu songMenu);

    boolean removeSongMenu(long songMenuId);

    boolean saveSongToSongMenu(long songId, long songMenuId);

    boolean removeSongToSongMenu(long songId, long songMenuId);

    boolean reverseAuthority(long songMenuId);
}
