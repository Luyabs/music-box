package com.example.musicbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.Song;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface SongService extends IService<Song> {

    boolean upLoadSongFile(MultipartFile songFile);            //上传新歌曲文件

    boolean upLoadSongCover(MultipartFile songCoverFile,Long songID);       //上传（修改）歌曲封面

    boolean changeOwnSongInfo(Song newSong);           //修改用户自己上传的歌曲信息

    boolean setVisibility(Long musicId, Integer status);//设置歌曲可见度

    IPage<Song> pageSong(int currentPage, int pageSize, Song condition);    // 分页获取歌曲

    void playUnVipSong(long songId, HttpServletResponse response);

    void playVipSong(long songId, HttpServletResponse response);

    void downloadUnVipSong(long songId, HttpServletResponse response);

    void downloadVipSong(long songId, HttpServletResponse response);
}
