package com.example.musicbox.controller;

import com.example.musicbox.common.NeedToken;
import com.example.musicbox.entity.Album;
import com.example.musicbox.entity.Song;
import com.example.musicbox.common.Result;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.service.AlbumService;
import com.example.musicbox.service.SongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;


@RestController
@Api("上传")
@RequestMapping("/compose")
public class ComposeController {
    @Autowired
    private SongService songService;
    @Autowired
    private AlbumService albumService;

    @ApiOperation(value = "上传歌曲",notes = "[token] 需传入MultipartFile文件流")
    @NeedToken
    @PostMapping("/music")
    public Result upLoadSongFile(@RequestPart("songFile")MultipartFile songFile){

        boolean res = songService.upLoadSongFile(songFile);
        return res?Result.success().message("上传歌曲文件成功"):Result.error().message("上传歌曲文件失败");
    }
    @ApiOperation(value = "上传歌曲封面",notes = "[token]需传入MultipartFile文件流")
    @NeedToken
    @PostMapping("/cover")
    public Result upLoadSongCover(@RequestPart("songCoverFile")MultipartFile songCoverFile, @RequestParam String songID){
        Long songId  = Long.parseLong(songID);
        boolean res = songService.upLoadSongCover(songCoverFile,songId);
        return res?Result.success().message("上传歌曲封面成功"):Result.error().message("上传歌曲封面失败");
    }

    @ApiOperation(value = "分页获取自己上传的歌曲",notes = "[token]需要传入页号，页大小")
    @NeedToken
    @GetMapping("/page")
    public Result getOwnUploadedSong(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        ///**有待完成**///
        return Result.success();
    }

    @ApiOperation(value = "修改自己上传的歌曲信息", notes = "[token]")
    @NeedToken
    @PutMapping
    public Result changeOwnSongInfo(@RequestBody Song song){
        boolean res = songService.changeOwnSongInfo(song);
        return res?Result.success().message("修改歌曲信息成功"):Result.error().message("修改用户信息失败");
    }

    @ApiOperation(value = "删除自己上传的歌曲", notes = "[token]")
    @NeedToken
    @DeleteMapping("/{music_id}")
    public Result deleteOwnSongInfo(@PathVariable("music_id") long musicId){
        boolean res = songService.deleteOwnSongInfo(musicId);
        return res?Result.success().message("删除歌曲成功"):Result.error().message("删除歌曲失败");
    }
    @ApiOperation(value = "设置歌曲可见度", notes = "[token]")
    @NeedToken
    @DeleteMapping("/visibility")
    public Result setVisibility(long musicId,int status){
        boolean res = songService.setVisibility(musicId,status);
        return res?Result.success().message("设置可见度成功"):Result.error().message("设置可见度失败");
    }

//    @ApiOperation(value = "创建专辑" , notes = "[token]传入一个包含专辑和歌单对象的map")
//    @NeedToken
//    @PostMapping("/album")
//    public Result createAlbum(@RequestBody HashMap<String, Object>map){
//        Album newAlbum = (Album) map.get("album");
//        SongMenu newSongMenu = (SongMenu) map.get("songMenu");
//        boolean res = albumService.createAlbum(newSongMenu,newAlbum);
//        return res?Result.success().message("创建专辑成功"):Result.error().message("创建专辑失败");
//
//    }



}
