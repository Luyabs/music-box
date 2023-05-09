package com.example.musicbox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.musicbox.common.NeedToken;
import com.example.musicbox.common.Result;
import com.example.musicbox.entity.Song;
import com.example.musicbox.service.SongMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("歌单/公共")
@RequestMapping("/menu/history")
public class MenuHistoryController {
    @Autowired
    private SongMenuService songMenuService;

    @NeedToken
    @ApiOperation(value = "分页获取指定用户历史播放记录", notes = "page_number, page_size, 没有条件")
    @GetMapping
    public Result pageHistory(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Song> page = songMenuService.pageHistory(currentPage, pageSize);
        return Result.success().data("history", page);
    }

    @NeedToken
    @ApiOperation(value = "分页获取指定用户历史播放记录", notes = "page_number, page_size, 没有条件")
    @DeleteMapping
    public Result deleteHistory() {
        boolean res = songMenuService.removeHistory();
        return res ? Result.success().message("清空历史成功") : Result.error();
    }
}
