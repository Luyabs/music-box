package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.dto.AlbumDto;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.service.SongMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("歌单/收藏")
@RequestMapping("/menu/collect")
public class MenuCollectController {
    @Autowired
    private SongMenuService songMenuService;

    @NeedToken
    @ApiOperation(value = "分页获取自己收藏的所有歌单的信息", notes = "page_number, page_size, 不携带条件")
    @GetMapping("/page/song_menu")
    public Result pageSongMenu(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<SongMenu> page = songMenuService.getCollectedSongMenuPage(currentPage, pageSize);
        return Result.success().data("song_menu_page", page);
    }

    @NeedToken
    @ApiOperation(value = "分页获取自己收藏的所有专辑的信息", notes = "page_number, page_size, 不携带条件")
    @GetMapping("/page/album")
    public Result pageAlbum(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<AlbumDto> page = songMenuService.getCollectedAlbumPage(currentPage, pageSize);
        return Result.success().data("album_page", page);
    }

    @NeedToken
    @ApiOperation(value = "按id获取自己收藏的歌单(包括专辑)的信息 (含歌曲)", notes = "[包括专辑] 会包含歌曲")
    @GetMapping("/{song_menu_id}}")
    public Result getDetailedSongMenu(@PathVariable("song_menu_id") long songMenuId) {
        SongMenuDto songMenuDto = songMenuService.getCollectedSongMenuDtoById(songMenuId);
        return Result.success().data("detailed_menu", songMenuDto);
    }
    @NeedToken
    @ApiOperation(value = "收藏歌单 (或专辑)", notes = "[token] {song_menu_id}（自己可以收藏自己创建的歌单）")
    @PostMapping("/{song_menu_id}}")
    public Result collectSongMenu(@PathVariable("song_menu_id") long songMenuId){
        boolean res = songMenuService.collectSongMenu(songMenuId);
        return res ? Result.success().message("收藏歌单成功") : Result.error().message("收藏歌单失败");
    }

    @NeedToken
    @ApiOperation(value = "不再收藏歌单 (或专辑)", notes = "[token] {song_menu_id}（自己可以不收藏自己创建的歌单）")
    @DeleteMapping("/{song_menu_id}}")
    public Result cancelCollectMenu(@PathVariable("song_menu_id") long songMenuId){
        boolean res = songMenuService.cancelCollectSongMenu(songMenuId);
        return res ? Result.success().message("取消收藏歌单成功") : Result.error().message("取消收藏歌单失败");
    }

}
