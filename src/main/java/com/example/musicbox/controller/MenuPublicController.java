package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
@Api("歌单/公共")
@RequestMapping("/menu/public")
public class MenuPublicController {
    @Autowired
    private SongMenuService songMenuService;


    @ApiOperation(value = "分页获取任意公开的歌单信息", notes = "page_number, page_size, *条件 允许根据menu_name与menu_introduction进行查询")
    @GetMapping("/page/song_menu")
    public Result pageSongMenu(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, SongMenu condition) {
        IPage<SongMenu> page = songMenuService.getPublicSongMenuPage(currentPage, pageSize, condition);
        return Result.success().data("song_menu_page", page);
    }

    @ApiOperation(value = "分页获取任意公开的专辑信息", notes = "page_number, page_size, *条件 允许根据menu_name与album_introduction进行查询")
    @GetMapping("/page/album")
    public Result pageAlbum(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, AlbumDto condition) {
        IPage<AlbumDto> page = songMenuService.getPublicAlbumDtoPage(currentPage, pageSize, condition);
        return Result.success().data("album_page", page);
    }

    @ApiOperation(value = "获取指定id公开的歌单 (或专辑) 信息 (需包含歌单中的歌曲)", notes = "会包含歌曲")
    @GetMapping("/{song_menu_id}}")
    public Result getDetailedSongMenu(@PathVariable("song_menu_id") long songMenuId) {
        SongMenuDto songMenuDto = songMenuService.getPublicSongMenuDtoById(songMenuId);
        return Result.success().data("detailed_menu", songMenuDto);
    }



}
