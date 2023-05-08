package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.musicbox.common.Result;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.service.SongMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("歌单")
@RequestMapping("/menu/public")
public class MenuPublicController {
    @Autowired
    private SongMenuService songMenuService;


    @ApiOperation(value = "分页获取任意公开的歌单信息 (包含歌单中的歌曲)", notes = "page_number, page_size, *条件 允许根据menu_name与menu_introduction进行查询")
    @GetMapping("/page/song_menu")
    public Result pageSongMenu(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, SongMenu condition) {
        IPage<SongMenuDto> page = songMenuService.getPublicSongMenuPage(currentPage, pageSize, condition);
        return Result.success().data("song_menu_page", page);
    }


}
