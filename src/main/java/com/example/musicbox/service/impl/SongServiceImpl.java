package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.entity.Song;
import com.example.musicbox.mapper.SongMapper;
import com.example.musicbox.mapper.UserMapper;
import com.example.musicbox.service.SongService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {
    @Autowired
    private SongMapper songMapper;

    @Autowired
    private UserMapper userMapper;

    @Value("${file-url.song-base-url}")
    private String songBaseUrl;     // 音乐上传地址

    @Value("${file-url.cover-base-url}")
    private String coverBaseUrl;     // 封面上传地址

    @Override
    public boolean upLoadSongFile(MultipartFile songFile){
        new File(songBaseUrl).mkdirs();  // 没有文件夹就创一个
        if (!userMapper.selectById(UserInfo.get()).getIsCreator())
            throw new ServiceException("你还未加入创作者，无法上传作品");

        String originFileName = songFile.getOriginalFilename();   //原文件名
        String newFileName;      //新文件名
        String prefix;           //后缀名
        File localFile;          //本地文件对象
        Song newSong;
        int index;
        List<String> songPrefix = List.of("mp3", "wav", "wma", "m4a", "flac");
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
            newFileName = songBaseUrl + randomFileName +'.'+prefix;
            localFile = new File(newFileName);
            songFile.transferTo(localFile);
            //歌曲记录初始化：id，后台文件路径，歌曲名默认是上传文件名,封面默认为no_picture_yet.jpg,歌手名未知
            newSong = new Song().setUserId(UserInfo.get()).
                    setFileDirectory(newFileName).
                    setSongName(originFileName.substring(0,index)).
                    setCoverPicture("src/main/resources/static/no_picture_yet.jpg").
                    setSingerName("未知");
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
        return songMapper.insert(newSong)>0;
    }
    @Override
    public boolean upLoadSongCover(MultipartFile songCoverFile, Long songID){
        new File(coverBaseUrl).mkdirs();  // 没有文件夹就创一个

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
            newFileName = coverBaseUrl + randomFileName +'.'+prefix;             //重构图片名
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

    @Override
    public IPage<Song> pageSong(int currentPage, int pageSize, Song condition) {
        QueryWrapper<Song> wrapper = new QueryWrapper<Song>()
                .like(condition.getSingerName() != null, "singer_name", condition.getSingerName())
                .like(condition.getSongName() != null, "song_name", condition.getSongName())
                .like(condition.getLanguage() != null, "language", condition.getLanguage())
                .like(condition.getClassification() != null, "classification", condition.getClassification())
                .orderByDesc("update_time");
        IPage<Song> songPage = songMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);
        return songPage;
    }

    @Override
    public void playUnVipSong(long songId, HttpServletResponse response) {
        Song songInfo = songMapper.selectById(songId);
        if (songInfo == null)
            throw new ServiceException("不存在id=" + songId + "的歌曲");

        if (songInfo.getIsVip())
            throw new ServiceException("本URL不能播放VIP歌曲");

        transmitSong(songInfo, response, true);
    }

    @Override
    public void playVipSong(long songId, HttpServletResponse response) {
        Song songInfo = songMapper.selectById(songId);
        if (songInfo == null)
            throw new ServiceException("不存在id=" + songId + "的歌曲");

        if (songInfo.getIsVip() && !userMapper.selectById(UserInfo.get()).getIsVip() && songInfo.getUserId() != UserInfo.get())
            throw new ServiceException("本歌曲需要vip权限才能播放");  // 作者本人允许播放

        transmitSong(songInfo, response, true);
    }

    @Override
    public void downloadUnVipSong(long songId, HttpServletResponse response) {
        Song songInfo = songMapper.selectById(songId);
        if (songInfo == null)
            throw new ServiceException("不存在id=" + songId + "的歌曲");

        if (songInfo.getIsVip())
            throw new ServiceException("本URL不能下载VIP歌曲");

        transmitSong(songInfo, response, false);
    }

    @Override
    public void downloadVipSong(long songId, HttpServletResponse response) {
        Song songInfo = songMapper.selectById(songId);
        if (songInfo == null)
            throw new ServiceException("不存在id=" + songId + "的歌曲");

        if (songInfo.getIsVip() && !userMapper.selectById(UserInfo.get()).getIsVip() && songInfo.getUserId() != UserInfo.get())
            throw new ServiceException("本歌曲需要vip权限才能下载");   // 作者本人允许下载

        transmitSong(songInfo, response, false);
    }

    @SneakyThrows
    private void transmitSong(Song songInfo, HttpServletResponse response, boolean online) {
        URL url = new URL("file:///" + songInfo.getFileDirectory());
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        String[] fileUrlSegments = songInfo.getFileDirectory().split("\\.");
        response.reset();
        response.setContentType(conn.getContentType());
        response.setHeader("Content-Disposition",
                online ? "inline; filename=" + URLEncoder.encode(songInfo.getSongName(), StandardCharsets.UTF_8)
                        : "attachment; filename=" + URLEncoder.encode(songInfo.getSongName().replace(' ', '_') + "." + fileUrlSegments[fileUrlSegments.length - 1], StandardCharsets.UTF_8)
        );
        OutputStream outputStream = response.getOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
    }


}
