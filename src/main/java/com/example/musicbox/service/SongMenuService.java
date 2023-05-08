package com.example.musicbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.SongMenu;

public interface SongMenuService extends IService<SongMenu> {

    IPage<SongMenuDto> getPublicSongMenuPage(int currentPage, int pageSize, SongMenu condition);
}
