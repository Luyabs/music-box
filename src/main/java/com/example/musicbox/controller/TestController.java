package com.example.musicbox.controller;

import com.example.musicbox.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/123")
    public String hi() {
        log.info("hi");
        return "hi";
    }

    @GetMapping("/456")
    public Result hi2() {
        return Result.success().data("啊", "123");
    }
}
