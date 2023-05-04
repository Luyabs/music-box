package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.entity.Song;
import com.example.musicbox.mapper.SongMapper;
import com.example.musicbox.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {
    @Autowired
    private SongMapper songMapper;


    @Override
    public boolean upLoadSongFile(MultipartFile songFile){
        String FileDirName = "E:/我的资源/java实践/MusicBox项目与资源/song_download/";      //文件目录名
        String originFileName = songFile.getOriginalFilename();   //原文件名
        String newFileName;      //新文件名
        String prefix;           //后缀名
        File localFile;          //本地文件对象
        Song newSong;
        int index;
        ArrayList<String> songPrefix = new ArrayList<>();
        songPrefix.add("mp3");
        songPrefix.add("wav");
        songPrefix.add("wma");
        songPrefix.add("m4a");
        songPrefix.add("flac");
        try{
            if(originFileName!=null){
                index = originFileName.lastIndexOf('.');
                prefix =originFileName.substring(index+1);
                if(index<=0 || !songPrefix.contains(prefix))
                    throw new ServiceException("文件格式错误");
            }
            else{
                throw new ServiceException("文件名错误（不能为空）");
            }
            String randomFileName = UUID.randomUUID().toString();               //服务器本地歌曲名随机
            newFileName = FileDirName + randomFileName +'.'+prefix;
            localFile = new File(newFileName);
            songFile.transferTo(localFile);
            //歌曲记录初始化：id，后台文件路径，歌曲名默认是上传文件名,封面默认为no_picture_yet.jpg,歌手名未知
            newSong = new Song().setUserId(UserInfo.get()).
                    setFileDirectory(newFileName).
                    setSongName(originFileName.substring(0,index)).
                    setCoverPicture("src/main/resources/no_picture_yet.jpg").
                    setSingerName("未知");
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
        return songMapper.insert(newSong)>0;
    }
    @Override
    public boolean upLoadSongCover(MultipartFile songCoverFile,Long songID){
        String FileDirName = "E:/我的资源/java实践/MusicBox项目与资源/song_cover_download/";      //文件目录名
        String originFileName = songCoverFile.getOriginalFilename();   //原文件名
        String newFileName;      //新文件名
        String prefix;           //后缀名
        File localFile;          //本地文件对象
        int index;
        Song song;
        try{
            if(originFileName!=null){
                index = originFileName.lastIndexOf('.');
                prefix =originFileName.substring(index+1);
                if(index<=0 || !prefix.equals("jpg"))
                    throw new ServiceException("歌曲封面格式错误（只能为jpg）");
            }
            else{
                throw new ServiceException("文件名错误（不能为空）");
            }
            String randomFileName = UUID.randomUUID().toString();               //服务器本地歌曲封面随机
            newFileName = FileDirName + randomFileName +'.'+prefix;             //重构图片名
            localFile = new File(newFileName);
            songCoverFile.transferTo(localFile);
            song = songMapper.selectById(songID);
            if(!song.getUserId().equals(UserInfo.get()))
                throw new ServiceException("当前用户无权限修改他人歌曲");
            else
                song.setCoverPicture(newFileName);                              //若当前用户是歌曲的创建者，修改封面

        }catch (Exception ex){
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
        return songMapper.updateById(song)>0;
    }

    @Override
    public boolean changeOwnSongInfo(Song newSong){
        long userId = UserInfo.get();
        if(!newSong.getUserId().equals(userId))     //若需要修改信息的歌曲的用户id和当前请求的用户id不符，抛异常
            throw new ServiceException("当前用户无权限修改他人歌曲/无法修改上传者id");
        newSong.setFileDirectory(null).setCreateTime(null).setStatus(null).setCreateTime(null).setCoverPicture(null);
        return songMapper.updateById(newSong) > 0;
    }
    @Override
    public boolean setVisibility(Long musicId,Integer status){
        Long userId = UserInfo.get();
        Song song = songMapper.selectById(musicId);
        if(status<0||status>5)
            throw new ServiceException("设置歌曲信息（状态）异常");
        if(song == null)
            throw new ServiceException("歌曲不存在");
        if(!userId.equals(song.getUserId()))
            throw new ServiceException("当前用户无权限修改他人歌曲信息（状态）");

        song.setStatus(status);             //修改歌曲状态
        return songMapper.updateById(song)>0;

    }
}
