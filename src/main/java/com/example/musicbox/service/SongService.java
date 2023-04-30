package com.example.musicbox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.entity.Song;
import org.springframework.web.multipart.MultipartFile;

public interface SongService extends IService<Song> {

    boolean upLoadSongFile(MultipartFile songFile);            //上传新歌曲文件

    boolean upLoadSongCover(MultipartFile songCoverFile,Long songID);       //上传（修改）歌曲封面

    boolean changeOwnSongInfo(Song newSong);           //修改用户自己上传的歌曲信息
}
