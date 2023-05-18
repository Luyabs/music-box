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
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api("歌单/我的")
@RequestMapping("/menu/my")
public class MenuMyController {
    @Autowired
    private SongMenuService songMenuService;

    @ApiOperation(value = "上传歌单封面",notes = "[token]需传入MultipartFile文件流")
    @NeedToken
    @PostMapping("/cover")
    public Result upLoadSongCover(@RequestPart MultipartFile songMenuCoverFile, @RequestParam long songMenuId){

        boolean res = songMenuService.upLoadSongMenuCover(songMenuCoverFile,songMenuId);
        return res ?Result.success().message("上传歌单封面成功"):Result.error().message("上传歌单封面失败");

    }

    @NeedToken
    @ApiOperation(value = "分页获取自己创建的歌单信息", notes = "page_number, page_size, *条件 允许根据menu_name与menu_introduction进行查询")
    @GetMapping("/page/song_menu")
    public Result pageSongMenu(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, SongMenu condition) {
        IPage<SongMenu> page = songMenuService.getMySongMenuPage(currentPage, pageSize, condition);
        return Result.success().data("song_menu_page", page);
    }

    @NeedToken
    @ApiOperation(value = "分页获取自己创建的专辑信息", notes = "page_number, page_size, *条件 允许根据menu_name与album_introduction进行查询")
    @GetMapping("/page/album")
    public Result pageAlbum(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, AlbumDto condition) {
        IPage<AlbumDto> page = songMenuService.getMyAlbumDtoPage(currentPage, pageSize, condition);
        return Result.success().data("album_page", page);
    }

    @NeedToken
    @ApiOperation(value = "按id获取自己创建的歌单(包括专辑)的信息 (含歌曲)", notes = "[包括专辑] 会包含歌曲")
    @GetMapping("/{song_menu_id}}")
    public Result getDetailedSongMenu(@PathVariable("song_menu_id") long songMenuId) {
        SongMenuDto songMenuDto = songMenuService.getMySongMenuDtoById(songMenuId);
        return Result.success().data("detailed_menu", songMenuDto);
    }

    @NeedToken
    @ApiOperation(value = "新建歌单", notes = "不包含歌曲")
    @PostMapping
    public Result createSongMenu(@RequestBody SongMenu songMenu) {
        boolean res = songMenuService.saveSongMenu(songMenu);
        return res ? Result.success().message("新建成功") : Result.error();
    }

    @NeedToken
    @ApiOperation(value = "编辑歌单信息", notes = "不包含歌曲")
    @PutMapping
    public Result editSongMenu(@RequestBody SongMenu songMenu) {
        boolean res = songMenuService.editSongMenu(songMenu);
        return res ? Result.success().message("更改成功") : Result.error();
    }

    @NeedToken
    @ApiOperation(value = "删除自己创建的歌单", notes = "仅置 authority = -1")
    @DeleteMapping("/{song_menu_id}")
    public Result deleteSongMenu(@PathVariable("song_menu_id") long songMenuId) {
        boolean res = songMenuService.removeSongMenu(songMenuId);
        return res ? Result.success().message("删除成功") : Result.error();
    }

    @NeedToken
    @ApiOperation(value = "向指定歌单中添加歌曲", notes = "添加歌曲到歌单")
    @PostMapping("/song")
    public Result addSongToSongMenu(@RequestParam long songId, @RequestParam long songMenuId) {
        boolean res = songMenuService.saveSongToSongMenu(songId, songMenuId);
        return res ? Result.success().message("歌曲添加成功") : Result.error();
    }

    @NeedToken
    @ApiOperation(value = "删除指定歌单中指定歌曲 ", notes = "从歌单删除歌曲")
    @DeleteMapping("/song")
    public Result deleteSongFromSongMenu(@RequestParam long songId, @RequestParam long songMenuId) {
        boolean res = songMenuService.removeSongToSongMenu(songId, songMenuId);
        return res ? Result.success().message("歌曲移除成功") : Result.error();
    }


    @NeedToken
    @ApiOperation(value = "反转歌单可见度", notes = "私有authority = 1 <==> 公开 authority = 0")
    @PostMapping("/authority/{song_menu_id}")
    public Result reverseAuthority(@PathVariable("song_menu_id") long songMenuId) {
        boolean res = songMenuService.reverseAuthority(songMenuId);
        return res ? Result.success().message("可见度反转成功") : Result.error();
    }
}
