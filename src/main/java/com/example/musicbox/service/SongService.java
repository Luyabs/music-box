package com.example.musicbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.SongComment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface SongService extends IService<Song> {

    boolean upLoadSongFile(MultipartFile songFile);            //上传新歌曲文件

    boolean upLoadSongCover(MultipartFile songCoverFile,Long songID);       //上传（修改）歌曲封面

    boolean changeOwnSongInfo(Song newSong);           //修改用户自己上传的歌曲信息

    boolean deleteOwnSongInfo(long songID);             //删除自己上传的歌曲

    boolean setVisibility(long musicId, Integer status);//设置歌曲可见度

    IPage<Song> pageSong(int currentPage, int pageSize, Song condition);    // 分页获取歌曲

    void playSongGuest(long songId, HttpServletResponse response);

    void playSongLogged(long songId, HttpServletResponse response);

    void downloadSongGuest(long songId, HttpServletResponse response);

    void downloadSongLogged(long songId, HttpServletResponse response);

    boolean saveComment(long songId, String content);            //发布评论

    boolean changeComment(long commentId, String content);       //修改评论

    boolean deleteComment(long commentId);          //删除评论

    IPage<SongComment> songCommentPage(long songId, int currentPage, int pageSize, SongComment conditon);     //分页获取指定id的歌曲评论


}
