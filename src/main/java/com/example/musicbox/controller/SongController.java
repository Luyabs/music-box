package com.example.musicbox.controller;

import com.example.musicbox.common.Result;
import com.example.musicbox.service.SongService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("歌曲")
@RequestMapping("/song")
public class SongController {
    @Autowired
    private SongService songService;

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
