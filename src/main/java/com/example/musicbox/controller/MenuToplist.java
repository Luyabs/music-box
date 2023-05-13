package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.musicbox.common.Result;
import com.example.musicbox.dto.SongDto;
import com.example.musicbox.dto.SongMenuDtowithCollection;
import com.example.musicbox.entity.Song;
import com.example.musicbox.service.SongMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("排行榜")
@RequestMapping("/menu/toplist")
public class MenuToplist {

    @Autowired
    private SongMenuService songMenuService;

    @ApiOperation(value = "获取一定数量近日播放量较多的歌曲 (播放排行榜)",notes = "需要页面号，" +
            "页面大小，统计的天数和最小播放量；返回歌曲列表加对应的播放数（SongDto）")
    @GetMapping("/top_play")
    public Result getPlaylistByPlayColume(@RequestParam(defaultValue = "1") int page_number,
                              @RequestParam(defaultValue = "10") int page_size,
                              @RequestParam(defaultValue = "7") int days,
                              @RequestParam(defaultValue = "1") long minimumPlayVolume
                              ){
        IPage<SongDto> songList = songMenuService.
                getPlaylistByPlayVolume(page_number,page_size,days,minimumPlayVolume);
        return Result.success().message("获取"+days+"日内歌曲排行榜成功（收听次数）").data("topSonglist",songList);
    }

    @ApiOperation(value = "获取收藏量最多的歌",notes = "需要页面号，" +
            "页面大小，统计的天数和最小播放量；返回歌曲列表加对应的被收藏数（SongDto）")
    @GetMapping("/top_collect")
    public Result getPlaylistByCollection(@RequestParam(defaultValue = "1") int page_number,
                                          @RequestParam(defaultValue = "10") int page_size,
                                          @RequestParam(defaultValue = "7") int days,
                                          @RequestParam(defaultValue = "1") long minimumCollection){
        IPage<SongDto> songList = songMenuService.
                getPlaylistByCollection(page_number,page_size,days,minimumCollection);
        return Result.success().message("获取"+days+"日内歌曲排行榜成功（被收藏次数）").data("topSonglist",songList);
    }





    @ApiOperation(value = "获取收藏量最多的歌单" ,notes = "需要页面号，" +
            "页面大小，统计的天数和最小播放量;返回歌单列表加对应的被收藏数（SongMenuDtowithCollection）")
    @GetMapping("/top_menu")
    public Result getTopSongMenuList(@RequestParam(defaultValue = "1") int page_number,
                                     @RequestParam(defaultValue = "10") int page_size,
                                     @RequestParam(defaultValue = "7") int days,
                                     @RequestParam(defaultValue = "1") long minimumCollection){

        IPage<SongMenuDtowithCollection> menuList = songMenuService.
                getTopSongMenuList(page_number,page_size,days,minimumCollection) ;
        return Result.success().message("获取"+days+"日内歌单排行榜成功（被收藏次数）").data("topMenulist",menuList);
    }

    @ApiOperation(value = "获取收藏量最多的专辑" ,notes = "需要页面号，" +
            "页面大小，统计的天数和最小播放量;返回专辑列表加对应的被收藏数（SongMenuDtowithCollection）" +
            "注：不包含专辑独有信息")
    @GetMapping("/top_album")
    public Result getTopAlbumList(@RequestParam(defaultValue = "1") int page_number,
                                     @RequestParam(defaultValue = "10") int page_size,
                                     @RequestParam(defaultValue = "7") int days,
                                     @RequestParam(defaultValue = "1") long minimumCollection){
        IPage<SongMenuDtowithCollection> albumList = songMenuService.getTopAlbumList(page_number,page_size,days,minimumCollection);
        return Result.success().message("获取"+days+"日内专辑排行榜成功（被收藏次数）").data("topAlbumlist",albumList);
    }

}
