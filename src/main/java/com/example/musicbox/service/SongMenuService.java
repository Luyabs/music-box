package com.example.musicbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.dto.*;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.SongMenu;
import org.springframework.web.multipart.MultipartFile;

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

    IPage<Song> pageHistory(int currentPage, int pageSize);

    boolean removeHistory();

    IPage<SongMenu> getCollectedSongMenuPage(int currentPage, int pageSize);

    IPage<AlbumDto> getCollectedAlbumPage(int currentPage, int pageSize);

    SongMenuDto getCollectedSongMenuDtoById(long songMenuId);

    boolean collectSongMenu(long songMenuId);                     //用户收藏歌单
    boolean cancelCollectSongMenu(long songMenuId);               //用户取消收藏歌单

    IPage<SongDto> getPlaylistByPlayVolume(int currentPage, int pageSize,
                                           int days, long playVolume); //获取一定数量近日播放量较多的歌曲 (播放排行榜)
    IPage<SongDto> getPlaylistByCollection(int currentPage, int pageSize,
                               int days, long collection);             //获取收藏量最多的歌曲

    IPage<SongMenuDtowithCollection> getTopSongMenuList(int currentPage, int pageSize,
                                                        int days, long collection);//获取近日收藏量最多的歌单
    IPage<SongMenuDtowithCollection> getTopAlbumList (int currentPage, int pageSize,
                                                   int days, long collection);//获取近日收藏量最多的专辑
    boolean upLoadSongMenuCover(MultipartFile songMenuCoverFile, Long menuID);           //上传（修改）歌单（专辑）封面
}
