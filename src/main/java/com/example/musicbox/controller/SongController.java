package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.SongComment;
import com.example.musicbox.service.SongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @ApiOperation(value = "播放指定id歌曲 [未登录状态]", notes = "请直接在浏览器输入URL来测试这项 [无法下载VIP歌曲]")
    @GetMapping("/play/guest/{song_id}")
    public void playSong(@PathVariable("song_id") long songId, HttpServletResponse response) {
        songService.playSongGuest(songId, response);
    }

    @ApiOperation(value = "播放指定id歌曲 [限登录状态]", notes = "[需要token] 请直接在浏览器输入URL来测试这项(较难测试)")
    @NeedToken
    @GetMapping("/play/logged/{song_id}")
    public void playVipSong(@PathVariable("song_id") long songId, HttpServletResponse response) {
        songService.playSongLogged(songId, response);
    }


    @ApiOperation(value = "下载指定id歌曲 [未登录状态]", notes = "请直接在浏览器输入URL来测试这项 [无法下载VIP歌曲]")
    @GetMapping("/download/guest/{song_id}")
    public void downloadSong(@PathVariable("song_id") long songId, HttpServletResponse response) {
        songService.downloadSongGuest(songId, response);
    }

    @ApiOperation(value = "下载指定id歌曲 [限登录状态]", notes = "[需要token]")
    @NeedToken
    @GetMapping("/download/logged/{song_id}")
    public void downloadVipSong(@PathVariable("song_id") long songId, HttpServletResponse response) {
        songService.downloadSongLogged(songId, response);
    }


    @ApiOperation(value = "发表对指定歌曲的评论", notes = "[token] {song_id}, content")
    @PostMapping(value = "/comment/{song_id}")
    @NeedToken
    public Result comment(@PathVariable long song_id, String content) {
        boolean res = songService.saveComment(song_id, content);
        return res ? Result.success().message("发布成功") : Result.error().message("发布失败");
    }

    @ApiOperation(value = "修改对指定歌曲的评论", notes = "[token] {comment_id}, content")
    @PutMapping(value = "/comment")
    @NeedToken
    public Result changeComment(@RequestBody SongComment comment) {
        boolean res = songService.changeComment(comment);
        return res ? Result.success().message("修改成功") : Result.error().message("修改失败");
    }

    @ApiOperation(value = "删除对指定歌曲的评论", notes = "[token] {comment_id}")
    @DeleteMapping(value = "/comment/{comment_id}")
    @NeedToken
    public Result deleteComment(@PathVariable long comment_id) {
        boolean res = songService.deleteComment(comment_id);
        return res ? Result.success().message("删除成功") : Result.error().message("删除失败");
    }

    @ApiOperation(value = "分页获取指定id的歌曲评论", notes = "{song_id}, page_number, page_size, *条件 允许根据user_id和content查询")
    @GetMapping(value = "/comment/{song_id}")
    public Result getSongCommentPage(@PathVariable long song_id,
                                     @RequestParam(defaultValue = "1")int pageNumber,
                                     @RequestParam(defaultValue = "10")int pageSize,
                                     SongComment condition) {
        return Result.success().data("pages", songService.songCommentPage(song_id, pageNumber, pageSize, condition));
    }

    @Value("${file-url.song-base-url}")
    private String baseUrl;     // 图片基地址

    @GetMapping
    public Result getAll() {
        log.info("baseUrl: " + baseUrl);
        log.info("baseUrl: " + baseUrl);
        log.info("baseUrl: " + baseUrl);
        log.info("baseUrl: " + baseUrl);
        return Result.success().data("list", songService.list());
    }
}
