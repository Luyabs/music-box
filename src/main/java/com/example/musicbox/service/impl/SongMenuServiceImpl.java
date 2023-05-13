package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.dto.*;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.entity.relation.SongMenuComposition;
import com.example.musicbox.entity.relation.UserMenuCollection;
import com.example.musicbox.mapper.AlbumMapper;
import com.example.musicbox.mapper.SongMenuMapper;
import com.example.musicbox.mapper.UserMapper;
import com.example.musicbox.mapper.relation.SongMenuCompositionMapper;
import com.example.musicbox.mapper.relation.SongPlayRecordMapper;
import com.example.musicbox.mapper.relation.UserMenuCollectionMapper;
import com.example.musicbox.service.SongMenuService;
import com.example.musicbox.service.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SongMenuServiceImpl extends ServiceImpl<SongMenuMapper, SongMenu> implements SongMenuService {
    @Autowired
    private SongMenuMapper songMenuMapper;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private SongMenuCompositionMapper songMenuCompositionMapper;

    @Autowired
    private SongPlayRecordMapper songPlayRecordMapper;

    @Autowired
    private SongService songService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMenuCollectionMapper userMenuCollectionMapper;

    /**
     * 分页查询歌单 [公开的]
     */
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

    /**
     * 分页查询专辑 [公开的]
     */
    @Override
    public IPage<AlbumDto> getPublicAlbumDtoPage(int currentPage, int pageSize, AlbumDto condition) {
        QueryWrapper<AlbumDto> wrapper = new QueryWrapper<AlbumDto>()
                .eq("authority", 0)    // 公开
                .eq("is_album", true)  // 专辑
                .like(condition.getMenuName() != null, "menu_name", condition.getMenuName())    // 歌单名条件
                .like(condition.getMenuIntroduction() != null, "album_introduction", condition.getAlbumIntroduction())   // 专辑简介条件
                .orderByDesc("update_time") ;
        IPage<AlbumDto> albumDtoPage = albumMapper.selectPageDto(new Page<>(currentPage, pageSize), wrapper);
        return albumDtoPage;
    }

    /**
     * 按id查询歌单/专辑dto [公开的]
     */
    @Override
    public SongMenuDto getPublicSongMenuDtoById(long songMenuId) {
        SongMenuDto songMenuDto = songMenuMapper.selectPublicDtoById(songMenuId);
        if (songMenuDto == null)
            throw new ServiceException("不存在song_menu_id=" + songMenuId + "的歌单");
        return songMenuDto;
    }

    /**
     * 分页查询歌单 [自己的]
     */
    @Override
    public IPage<SongMenu> getMySongMenuPage(int currentPage, int pageSize, SongMenu condition) {
        QueryWrapper<SongMenu> wrapper = new QueryWrapper<SongMenu>()
                .ne("authority", -1)              // 未被逻辑删除
                .eq("user_id", UserInfo.get())    // 自己
                .eq("is_album", false)  // 非专辑歌单
                .like(condition.getMenuName() != null, "menu_name", condition.getMenuName())    // 歌单名条件
                .like(condition.getMenuIntroduction() != null, "menu_introduction", condition.getMenuIntroduction())   // 歌单简介条件
                .orderByDesc("update_time") ;
        IPage<SongMenu> songMenuPage = songMenuMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);
        return songMenuPage;
    }

    /**
     * 分页查询专辑 [自己的]
     */
    @Override
    public IPage<AlbumDto> getMyAlbumDtoPage(int currentPage, int pageSize, AlbumDto condition) {
        if (!userMapper.selectById(UserInfo.get()).getIsCreator())
            throw new ServiceException("你还未加入创作者");
        QueryWrapper<AlbumDto> wrapper = new QueryWrapper<AlbumDto>()
                .ne("authority", -1)              // 未被逻辑删除
                .eq("user_id", UserInfo.get())    // 自己的
                .eq("is_album", true)  // 专辑
                .like(condition.getMenuName() != null, "menu_name", condition.getMenuName())    // 歌单名条件
                .like(condition.getMenuIntroduction() != null, "album_introduction", condition.getAlbumIntroduction())   // 专辑简介条件
                .orderByDesc("update_time") ;
        IPage<AlbumDto> albumDtoPage = albumMapper.selectPageDto(new Page<>(currentPage, pageSize), wrapper);
        return albumDtoPage;
    }

    /**
     * 按id查询歌单/专辑dto [公开的]
     */
    @Override
    public SongMenuDto getMySongMenuDtoById(long songMenuId) {
        SongMenuDto songMenuDto = songMenuMapper.selectMyDtoById(songMenuId, UserInfo.get());
        if (songMenuDto == null)
            throw new ServiceException("不存在或你没有权限查看 song_menu_id=" + songMenuId + "的歌单/专辑");
        if (songMenuDto.getAuthority() == -1)
            throw new ServiceException("song_menu_id=" + songMenuId + "的歌单/专辑已被删除");
        return songMenuDto;
    }


    /**
     * 添加歌单
     */
    @Override
    public boolean saveSongMenu(SongMenu songMenu) {
        songMenu.setUserId(UserInfo.get()).setAuthority(0).setIsAlbum(false).setCreateTime(null);
        return songMenuMapper.insert(songMenu) > 0;
    }

    /**
     * 编辑歌单
     */
    @Override
    public boolean editSongMenu(SongMenu songMenu) {
        songMenu.setAuthority(null).setIsAlbum(null).setCreateTime(null);
        getMySongMenuDtoById(songMenu.getId());     // 仅作错误判断
        return songMenuMapper.updateById(songMenu) > 0;
    }

    /**
     * 逻辑删除歌单
     * 仅置authority = - 1
     */
    @Override
    public boolean removeSongMenu(long songMenuId) {
        SongMenuDto songMenuDto = getMySongMenuDtoById(songMenuId);// 仅作错误判断
        return songMenuMapper.updateById(songMenuDto.setAuthority(-1)) > 0;
    }

    /**
     * 向指定歌单中添加歌曲
     */
    @Override
    public boolean saveSongToSongMenu(long songId, long songMenuId) {
        getMySongMenuDtoById(songMenuId);                   // 仅作错误判断
        songService.isSongExistAndPublic(songId);           // 仅作错误判断
        if (songMenuCompositionMapper.exists(new QueryWrapper<SongMenuComposition>().eq("song_id", songId).eq("song_menu_id", songMenuId)))
            throw new ServiceException("song_id=" + songId + "的歌曲已存在于 song_menu_id=" + songMenuId + "的歌单");
        return songMenuCompositionMapper.insert(new SongMenuComposition().setSongId(songId).setSongMenuId(songMenuId)) > 0;
    }

    /**
     * 向指定歌单中移除歌曲
     */
    @Override
    public boolean removeSongToSongMenu(long songId, long songMenuId) {
        getMySongMenuDtoById(songMenuId);                   // 仅作错误判断
        songService.isSongExistAndPublic(songId);           // 仅作错误判断
        SongMenuComposition one = songMenuCompositionMapper.selectOne(new QueryWrapper<SongMenuComposition>().eq("song_id", songId).eq("song_menu_id", songMenuId));
        if (one == null)
            throw new ServiceException("song_id=" + songId + "的歌曲不存在于 song_menu_id=" + songMenuId + "的歌单");
        return songMenuCompositionMapper.deleteById(one) > 0;
    }

    /**
     * 反置歌单可见度
     * 私有authority = 1 <==> 公开 authority = 0
     */
    @Override
    public boolean reverseAuthority(long songMenuId) {
        SongMenu songMenu = getMySongMenuDtoById(songMenuId);// 仅作错误判断
        songMenu.setAuthority(Math.abs(1 - songMenu.getAuthority()));
        return songMenuMapper.updateById(songMenu) > 0;
    }

    /**
     * 分页获取用户播放历史
     * 目前只允许获取自己的播放历史
     */
    @Override
    public IPage<Song> pageHistory(int currentPage, int pageSize) {
        IPage<Song> songPage = songPlayRecordMapper.selectSongPageByUserId(UserInfo.get(), new Page<>(currentPage, pageSize));
        return songPage;
    }

    /**
     * 移除用户的历史播放记录 只置status = -1
     */
    @Override
    public boolean removeHistory() {
        songPlayRecordMapper.logicalDeleteHistory(UserInfo.get());
        return true;    // 必定清除成功
    }

    /**
     * 分页获取收藏的歌单
     */
    @Override
    public IPage<SongMenu> getCollectedSongMenuPage(int currentPage, int pageSize) {
        List<Long> ids = getCollectedSongMenuIds(UserInfo.get());
        IPage<SongMenu> songMenuPage = songMenuMapper.selectPage(new Page<>(currentPage, pageSize),
                new QueryWrapper<SongMenu>()
                        .in("id", ids)      // 收藏的
                        .eq("is_album", false)   // 非专辑歌单
                        .eq("authority", 0)     // 公开的
        );
        return songMenuPage;
    }

    /**
     * 分页获取收藏的专辑
     */
    @Override
    public IPage<AlbumDto> getCollectedAlbumPage(int currentPage, int pageSize) {
        List<Long> ids = getCollectedSongMenuIds(UserInfo.get());
        IPage<AlbumDto> albumPage = albumMapper.selectPageDto(new Page<>(currentPage, pageSize),
                new QueryWrapper<AlbumDto>()
                        .in("s.id", ids)      // 收藏的
                        .eq("is_album", true)   // 专辑歌单
                        .eq("authority", 0)     // 公开的
        );
        return albumPage;
    }


    @Override
    public SongMenuDto getCollectedSongMenuDtoById(long songMenuId) {
        SongMenuDto songMenuDto = songMenuMapper.selectCollectedDtoById(songMenuId, UserInfo.get());
        if (songMenuDto == null)
            throw new ServiceException("不存在或你没有权限查看 song_menu_id=" + songMenuId + "的歌单/专辑");
        if (songMenuDto.getAuthority() == -1)
            throw new ServiceException("song_menu_id=" + songMenuId + "的歌单/专辑已被删除");
        return songMenuDto;
    }

    /**
     * 获取 用户id收藏的歌单 的id列表
     * @param userId 用户id
     * @return 用户id收藏的歌单 的id列表
     */
    private List<Long> getCollectedSongMenuIds(long userId) {
        List<UserMenuCollection> collections = userMenuCollectionMapper.selectList(new QueryWrapper<UserMenuCollection>().eq("user_id", userId));
        List<Long> ids = new ArrayList<>();
        for (UserMenuCollection collection : collections) {
            ids.add(collection.getSongMenuId());
        }
        return ids;
    }
    @Override
    public boolean collectSongMenu(long songMenuId){
        SongMenu songMenu = getSongMenuById(songMenuId);
        if(songMenu.getAuthority() < 0)
            throw new ServiceException("歌单状态异常，无法被收藏");
        if(songMenu.getAuthority() > 0)
            throw new ServiceException("歌单为私有歌单，无法被收藏");
        if(userMenuCollectionMapper.getIfUserCollectMenu(UserInfo.get(),songMenuId) > 0)
            throw new ServiceException("当前歌单已被收藏，无法被再次收藏");
        UserMenuCollection userMenuCollection = new UserMenuCollection().
                setUserId(UserInfo.get()).
                setSongMenuId(songMenuId);
        return userMenuCollectionMapper.insert(userMenuCollection) == 1;
    }
    @Override
    public boolean cancelCollectSongMenu(long songMenuId){
        SongMenu songMenu = getSongMenuById(songMenuId);
        if(songMenu.getAuthority() < 0)
            throw new ServiceException("歌单状态异常，无法被取消收藏");
        if(songMenu.getAuthority() > 0)
            throw new ServiceException("歌单为私有歌单，无法被取消收藏");
        if(userMenuCollectionMapper.getIfUserCollectMenu(UserInfo.get(),songMenuId) == 0)
            throw new ServiceException("当前歌单未被收藏，无法被再次取消收藏");
        QueryWrapper<UserMenuCollection> wrapper = new QueryWrapper<UserMenuCollection>().
                eq("user_id",UserInfo.get()).
                eq("song_menu_id",songMenuId);
        return userMenuCollectionMapper.delete(wrapper) == 1;           //删除收藏记录
    }
    @Override
    public IPage<SongDto> getPlaylistByPlayVolume(int currentPage, int pageSize,
                                                  int days, long minPlayVolume){
        IPage<SongDto> songDtoIPage = songPlayRecordMapper.
                selectSongToplistByPlayVolume(days,minPlayVolume,new Page<>(currentPage, pageSize));
        return songDtoIPage;
    }

    @Override
    public IPage<SongDto> getPlaylistByCollection(int currentPage, int pageSize,
                                                  int days, long minCollection){
        IPage<SongDto> songDtoIPage = userMenuCollectionMapper.
                getSongListByCollection(days,minCollection,new Page<>(currentPage, pageSize));
        return songDtoIPage;
    }
    @Override
    public IPage<SongMenuDtowithCollection> getTopSongMenuList(int currentPage, int pageSize,
                                                               int days, long minCollection){
        IPage<SongMenuDtowithCollection> songMenuDtoIPage = userMenuCollectionMapper.
                getSongMenuListByCollection(days,minCollection,new Page<>(currentPage, pageSize));
        return songMenuDtoIPage;
    }

    @Override
    public IPage<SongMenuDtowithCollection> getTopAlbumList (int currentPage, int pageSize,
                                                          int days, long minCollection){
        IPage<SongMenuDtowithCollection> albumDtoIPage = userMenuCollectionMapper.
                getAlbumListByCollection(days,minCollection,new Page<>(currentPage, pageSize));
                return albumDtoIPage;
    }
    private SongMenu getSongMenuById(long songMenuId){
        SongMenu songMenuInfo = songMenuMapper.selectById(songMenuId);
        if(songMenuInfo == null){
            throw new ServiceException("不存在id = "+songMenuId+"的歌单");
        }
        return songMenuInfo;
    }

}
