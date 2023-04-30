package com.example.musicbox.controller;

import com.example.musicbox.common.NeedToken;
import com.example.musicbox.entity.Song;
import com.example.musicbox.common.Result;
import com.example.musicbox.service.AlbumService;
import com.example.musicbox.service.SongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    public Result upLoadSongCover(@RequestPart("songCoverFile")MultipartFile songCoverFile,@RequestPart("歌曲ID") Long songID){
        boolean res = songService.upLoadSongCover(songCoverFile,songID);
        return res?Result.success().message("上传歌曲封面成功"):Result.error().message("上传歌曲封面失败");
    }

    @ApiOperation(value = "分页获取自己上传的歌曲",notes = "[token]需要传入页号，页大小")
    @NeedToken
    @GetMapping("/page")
    public Result getOwnUploadedSong(int pageNum,int pageSize){
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

    @ApiOperation(value = "删除自己上传的歌曲信息", notes = "[token]")
    @NeedToken
    @DeleteMapping("/{music_id}")
    public Result deleteOwnSongInfo(@PathVariable("music_id") long musicId){
        ///**有待完成**///
        return Result.success();
    }

//    @ApiOperation(value = "")


}
