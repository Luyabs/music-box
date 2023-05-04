package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.entity.Song;
import com.example.musicbox.service.SongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@Api("歌曲")
@RequestMapping("/song")
public class SongController {
    @Autowired
    private SongService songService;

    @ApiOperation(value = "分页获取歌曲信息 (非播放)", notes = "分页条件查询, 允许带singerName, songName, language, classification条件, 此项不支持按专辑名查询  [!注意此url不会返回图片，请根据返回信息中的coverPicture(e.g. D:/tmp/cover/f1c06.jpg) 去类似的访问如下路径: 127.0.0.1:8080/cover/f1c06.jpg]")
    @GetMapping("/page")
    public Result getSongPage(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, Song condition) {
        IPage<Song> songPage = songService.pageSong(currentPage, pageSize, condition);
        return Result.success().data("song_page", songPage);
    }

    @ApiOperation(value = "按id获取歌曲信息 (非播放)", notes = "id查询 [!注意此url不会返回图片，请根据返回信息中的coverPicture(e.g. D:/tmp/cover/f1c06.jpg) 去类似的访问如下路径: 127.0.0.1:8080/cover/f1c06.jpg]")
    @GetMapping("/{song_id}")
    public Result getSongPage(@PathVariable("song_id") long songId) {
        Song songInfo = songService.getById(songId);
        return Result.success().data("song_info", songInfo);
    }

    @ApiOperation(value = "播放指定id非vip歌曲", notes = "请直接在浏览器输入URL来测试这项")
    @GetMapping("/play/{song_id}")
    public void playSong(@PathVariable("song_id") long songId, HttpServletResponse response) {
        songService.playUnVipSong(songId, response);
    }

    @ApiOperation(value = "播放指定id的vip歌曲", notes = "[需要token] 请直接在浏览器输入URL来测试这项(较难测试)")
    @NeedToken
    @GetMapping("/play/vip/{song_id}")
    public void playVipSong(@PathVariable("song_id") long songId, HttpServletResponse response) {
        songService.playVipSong(songId, response);
    }


    @ApiOperation(value = "下载指定id非vip歌曲", notes = "[需要token]")
//    @NeedToken
    @GetMapping("/download/{song_id}")
    public void downloadSong(@PathVariable("song_id") long songId, HttpServletResponse response) {
        songService.downloadUnVipSong(songId, response);
    }

    @ApiOperation(value = "下载指定id的vip歌曲", notes = "[需要token]")
    @NeedToken
    @GetMapping("/download/vip/{song_id}")
    public void downloadVipSong(@PathVariable("song_id") long songId, HttpServletResponse response) {
        songService.downloadVipSong(songId, response);
    }


}
