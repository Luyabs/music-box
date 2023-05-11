package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.common.UserInfo;
import com.example.musicbox.common.exception.ServiceException;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.SongComment;
import com.example.musicbox.entity.relation.SongPlayRecord;
import com.example.musicbox.mapper.SongMapper;
import com.example.musicbox.mapper.UserMapper;
import com.example.musicbox.mapper.relation.SongCommentMapper;
import com.example.musicbox.mapper.relation.SongPlayRecordMapper;
import com.example.musicbox.service.SongService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private SongCommentMapper songCommentMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SongPlayRecordMapper songPlayRecordMapper;

    @Value("${file-url.song-base-url}")
    private String songBaseUrl;     // 音乐上传地址

    @Value("${file-url.cover-base-url}")
    private String coverBaseUrl;     // 封面上传地址

    @Override
    public boolean upLoadSongFile(MultipartFile songFile) {
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
        try {
            if (originFileName != null) {
                index = originFileName.lastIndexOf('.');
                prefix = originFileName.substring(index + 1);
                if (index <= 0 || !songPrefix.contains(prefix))
                    throw new ServiceException("文件格式错误");
            } else {
                throw new ServiceException("文件名错误（不能为空）");
            }
            String randomFileName = UUID.randomUUID().toString();               //服务器本地歌曲名随机
            newFileName = songBaseUrl + randomFileName + '.' + prefix;
            localFile = new File(newFileName);
            songFile.transferTo(localFile);
            //歌曲记录初始化：上传者id，后台文件路径，歌曲名默认是上传文件名,封面默认为no_picture_yet.jpg,歌手名未知
            newSong = new Song().setUserId(UserInfo.get()).
                    setFileDirectory(newFileName).
                    setSongName(originFileName.substring(0, index)).
                    setCoverPicture("src/main/resources/static/no_picture_yet.jpg").
                    setSingerName("未知");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
        return songMapper.insert(newSong) > 0;
    }

    @Override
    public boolean upLoadSongCover(MultipartFile songCoverFile, Long songID) {

        new File(coverBaseUrl).mkdirs();  // 没有文件夹就创一个

        String originFileName = songCoverFile.getOriginalFilename();   //原文件名
        String newFileName;      //新文件名
        String prefix;           //后缀名
        File localFile;          //本地文件对象
        int index;
        Song song;
        try {
            if (originFileName != null) {
                index = originFileName.lastIndexOf('.');
                prefix = originFileName.substring(index + 1);
                if (index <= 0 || !prefix.equals("jpg"))
                    throw new ServiceException("歌曲封面格式错误（只能为jpg）");
            } else {
                throw new ServiceException("文件名错误（不能为空）");
            }
            String randomFileName = UUID.randomUUID().toString();               //服务器本地歌曲封面随机
            newFileName = coverBaseUrl + randomFileName + '.' + prefix;             //重构图片名
            localFile = new File(newFileName);
            songCoverFile.transferTo(localFile);
            song = getSongById(songID);
            if (song.getStatus() < 0)
                throw new ServiceException("歌曲状态异常（删除/封禁），无法上传封面");
            if (!song.getUserId().equals(UserInfo.get()))
                throw new ServiceException("当前用户无权限修改他人歌曲");
            else
                song.setCoverPicture(newFileName);                              //若当前用户是歌曲的创建者，修改封面

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
        return songMapper.updateById(song) > 0;
    }

    @Transactional
    @Override
    public boolean deleteOwnSongInfo(long musicId) {
        long userId = UserInfo.get();
        Song song = getSongById(musicId);
        if (!song.getUserId().equals(userId))
            throw new ServiceException("当前用户无权限删除他人歌曲");
        SongComment songComment = new SongComment().
                setStatus(-1).                                               //更新歌曲下评论的状态为-1
                        setId(null).
                setCreateTime(null);
        QueryWrapper<SongComment> wrapper = new QueryWrapper<SongComment>().
                eq("song_id", musicId).
                ge("status", 0);                                  //所有状态正常（>0）的歌曲评论
        if (song.getStatus() < 0)
            throw new ServiceException("歌曲状态异常，用户无法删除歌曲");
        song.setStatus(-1);                                                 //更新歌曲状态为-1
        return songCommentMapper.update(songComment, wrapper) >=0 &&
                songMapper.updateById(song) == 1;
    }

    @Override
    public boolean changeOwnSongInfo(Song newSong) {
        long userId = UserInfo.get();
        Song originSong = getSongById(newSong.getId());           //查找被修改的歌曲状态
        if (originSong.getStatus() < 0)
            throw new ServiceException("歌曲状态异常，用户无法修改歌曲信息");
        if (!originSong.getUserId().equals(userId))     //若需要修改信息的歌曲的用户id和当前请求的用户id不符，抛异常
            throw new ServiceException("当前用户无权限修改他人歌曲/无法修改上传者id");
        newSong.setUserId(UserInfo.get()).          //歌曲用户无法修改
                setFileDirectory(null).             //歌曲路径无法修改
                setStatus(null).                    //歌曲状态无法修改
                setCreateTime(null).                //歌曲建立时间无法修改
                setCoverPicture(null);              //歌曲封面无法修改
        return songMapper.updateById(newSong) > 0;
    }

    @Override
    public boolean setVisibility(long musicId, Integer status) {
        Long userId = UserInfo.get();
        Song song = getSongById(musicId);
        if (!userId.equals(song.getUserId()))
            throw new ServiceException("当前用户无权限修改他人歌曲信息（状态）");
        if (song.getStatus() < 0)
            throw new ServiceException("歌曲状态异常，用户无法修改歌曲可见度");
        if (status < 0 || status > 3)
            throw new ServiceException("设置歌曲信息（状态）异常");

        song.setStatus(status);             //修改歌曲状态
        return songMapper.updateById(song) > 0;

    }

    @Override
    public IPage<Song> pageSong(int currentPage, int pageSize, Song condition) {
        QueryWrapper<Song> wrapper = new QueryWrapper<Song>()
                .eq(condition.getUserId() != null, "user_id", condition.getUserId())
                .like(condition.getSingerName() != null, "singer_name", condition.getSingerName())
                .like(condition.getSongName() != null, "song_name", condition.getSongName())
                .like(condition.getLanguage() != null, "language", condition.getLanguage())
                .like(condition.getClassification() != null, "classification", condition.getClassification())
                .orderByDesc("update_time");
        IPage<Song> songPage = songMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);
        return songPage;
    }
    @Override
    public void playSongGuest(long songId, HttpServletResponse response) {
        Song songInfo = getSongById(songId);

        if (songInfo.getIsVip())
            throw new ServiceException("未登录状态不能播放VIP歌曲");

        transmitSong(songInfo, response, true);
    }

    @Override
    public void playSongLogged(long songId, HttpServletResponse response) {
        Song songInfo = getSongById(songId);

        if (songInfo.getIsVip() && !userMapper.selectById(UserInfo.get()).getIsVip() && songInfo.getUserId() != UserInfo.get())
            throw new ServiceException("本歌曲需要vip权限才能播放");  // 作者本人允许播放

        transmitSong(songInfo, response, true);
    }

    @Override
    public void downloadSongGuest(long songId, HttpServletResponse response) {
        Song songInfo = getSongById(songId);

        if (songInfo.getIsVip())
            throw new ServiceException("未登录状态不能下载VIP歌曲");

        transmitSong(songInfo, response, false);
    }

    @Override
    public void downloadSongLogged(long songId, HttpServletResponse response) {
        Song songInfo = getSongById(songId);

        if (songInfo.getIsVip() && !userMapper.selectById(UserInfo.get()).getIsVip() && songInfo.getUserId() != UserInfo.get())
            throw new ServiceException("本歌曲需要vip权限才能下载");   // 作者本人允许下载

        transmitSong(songInfo, response, false);
    }

    /**
     * 按id获取歌曲 并校验是否为null
     */
    private Song getSongById(long songId) {
        Song songInfo = songMapper.selectById(songId);
        if (songInfo == null)
            throw new ServiceException("不存在id=" + songId + "的歌曲");
        return songInfo;
    }
    private SongComment getSongCommentById(long commentId) {
        SongComment songCommentInfo = songCommentMapper.selectById(commentId);
        if (songCommentInfo == null)
            throw new ServiceException("不存在id=" + commentId + "的评论");
        return songCommentInfo;
    }


    /**
     * 通过response传输歌曲文件
     */
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

        byte[] buffer = new byte[64];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();

        // 增加播放/下载记录
        if (UserInfo.isNull())      // 未登录用户不留下听歌记录
            return;
        SongPlayRecord record = new SongPlayRecord().setUserId(UserInfo.get()).setSongId(songInfo.getId());
        record.setStatus(online ? 0 : 1);
        songPlayRecordMapper.insert(record);
    }


    @Override
    public boolean saveComment(long songId, String content) {
        long userId = UserInfo.get();
        if (content == null || content.length() <= 0) {
            throw new ServiceException("评论内容不允许为空");
        }
        SongComment sc = new SongComment().setSongId(songId).setUserId(userId).setCommentsContent(content).setStatus(0);
        return songCommentMapper.insert(sc) > 0;
    }

    @Override
    public boolean changeComment(SongComment newComment) {
        long userId = UserInfo.get();
        SongComment oldComment = getSongCommentById(newComment.getId());
        if (userId != oldComment.getUserId()) {
            throw new ServiceException("当前用户无权限修改他人评论");
        }
        if(oldComment.getStatus() < 0)
            throw new ServiceException("评论状态异常，无法修改");
        String content = newComment.getCommentsContent();
        if ( content == null || content.length() <= 0) {
            throw new ServiceException("评论内容不允许为空");
        }
        oldComment.setCommentsContent(content);//将评论内容赋给原对象
        return songCommentMapper.updateById(oldComment) == 1;
    }

    @Override
    public boolean deleteComment(long commentId) {
        long userId = UserInfo.get();
        SongComment songComment = getSongCommentById(commentId);
        if (userId != songComment.getUserId()) {
            throw new ServiceException("当前用户无权限删除他人评论");
        }
        if(songComment.getStatus() < 0)
            throw new ServiceException("当前评论状态异常（删除/屏蔽）");
        songComment.setStatus(-1);                                //修改状态为-1
        return songCommentMapper.updateById(songComment) == 1;
    }

    @Override
    public IPage<SongComment> songCommentPage(long songId, int currentPage, int pageSize, SongComment conditon) {
        QueryWrapper<SongComment> wrapper = new QueryWrapper<SongComment>()
                .eq("song_id", songId)
                .eq(conditon.getUserId() != null, "user_id", conditon.getUserId())
                .like(conditon.getCommentsContent() != null, "comments_content", conditon.getCommentsContent())
                .orderByDesc("update_time");
        IPage<SongComment> songCommentPage = songCommentMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);
        return songCommentPage;
    }

    /**
     * 用于校验歌曲是否存在且为公开/自己的
     */
    @Override
    public Song isSongExistAndPublic(long songId) {
        Song song = songMapper.selectById(songId);
        if (song == null)
            throw new ServiceException("不存在song_id=" + songId + "的歌曲");
        switch (song.getStatus()) {
            case -1 -> throw new ServiceException("song_id=" + songId + "的已被删除");
            case 1 -> {
                if (song.getUserId() != UserInfo.get())
                    throw new ServiceException("song_id=" + songId + "的创作者未公开此曲");
            }
            case 0 -> {}
            default -> throw new ServiceException("status=" + song.getStatus() + "不合法");
        }
        return song;
    }
}
