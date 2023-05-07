package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.dto.AlbumDto;
import com.example.musicbox.entity.Album;
import com.example.musicbox.entity.Song;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

    @Autowired
    private SongMenuMapper songMenuMapper;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private SongMenuCompositionMapper songMenuCompositionMapper;

    @Transactional
    @Override
    public boolean createAlbum(AlbumDto albumDto) {
        boolean res1,res2;
        SongMenu newSongMenu;
        Album newAlbum = new Album();
        newSongMenu = albumDto;
        newSongMenu.setId(null).
                setAuthority(0).
                setIsAlbum(true).
                setUserId(UserInfo.get()).
                setCreateTime(null);
        res1 = songMenuMapper.insert(newSongMenu) == 1;         //新增歌单
        newAlbum.setId(newSongMenu.getId()).
                setAlbumIntroduction(albumDto.getAlbumIntroduction()).
                setIssueCompany(albumDto.getIssueCompany()).
                setIssueTime(albumDto.getIssueTime()).
                setCreateTime(null);
        res2 = albumMapper.insert(newAlbum) == 1;               //新增专辑
        return res1 && res2;
    }

    @Transactional
    @Override
    public boolean changeAlbumInfo(AlbumDto albumDto) {
        Album album = getAlbumById(albumDto.getId());
        SongMenu songMenu = getSongMenuById(albumDto.getId());
        if(!songMenu.getUserId().equals(UserInfo.get()))
            throw new ServiceException("用户无权限修改专辑");
        if(songMenu.getAuthority()<0)
            throw new ServiceException("专辑状态异常，无法修改专辑信息");
        songMenu.setAuthority(null).                                  //权限无法修改
                setMenuName(albumDto.getMenuName()).                  //修改歌单（专辑）名
                setMenuIntroduction(albumDto.getMenuIntroduction()).  //修改简介
                setIsAlbum(true);
        album.setAlbumIntroduction(albumDto.getAlbumIntroduction()).  //修改专辑简介
                setIssueCompany(albumDto.getIssueCompany()).          //修改发行公司
                setIssueTime(albumDto.getIssueTime()).                //修改发行时间
                setCreateTime(null);
        return albumMapper.updateById(album) == 1 &&
                songMenuMapper.updateById(songMenu) == 1;
    }

    @Override
    public boolean deleteAlbum(long songMenuId) {
        SongMenu songMenu = getSongMenuById(songMenuId);
        if(!songMenu.getUserId().equals(UserInfo.get()))
            throw new ServiceException("当前用户无权限删除他人专辑");
        if(songMenu.getAuthority()<0)
            throw new ServiceException("专辑状态异常，用户无法删除专辑");
        songMenu.setAuthority(-1);
        return songMenuMapper.updateById(songMenu) == 1;
    }
    private Album getAlbumById(long albumId) {
        Album albumInfo = albumMapper.selectById(albumId);
        if (albumInfo == null)
            throw new ServiceException("不存在id=" + albumId + "的专辑");
        return albumInfo;
    }
    private SongMenu getSongMenuById(long songMenuId) {
        SongMenu songMenuInfo = songMenuMapper.selectById(songMenuId);
        if (songMenuInfo == null)
            throw new ServiceException("不存在id=" + songMenuId + "的专辑");
        return songMenuInfo;
    }
}
