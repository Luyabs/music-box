package com.example.musicbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.musicbox.dto.SongMenuDto;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.mapper.SongMenuMapper;
import com.example.musicbox.service.SongMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SongMenuServiceImpl extends ServiceImpl<SongMenuMapper, SongMenu> implements SongMenuService {
    @Autowired
    private SongMenuMapper songMenuMapper;

    @Override
    public IPage<SongMenuDto> getPublicSongMenuPage(int currentPage, int pageSize, SongMenu condition) {
        QueryWrapper<SongMenuDto> wrapper = new QueryWrapper<SongMenuDto>()
                .eq("authority", 0)    // 公开
                .eq("is_album", false)  // 非专辑歌单
                .like(condition.getMenuName() != null, "menu_name", condition.getMenuName())    // 歌单名条件
                .like(condition.getMenuIntroduction() != null, "menu_introduction", condition.getMenuIntroduction())   // 歌单简介条件
                .orderByDesc("update_time") ;
        IPage<SongMenuDto> songMenuDtoPage = songMenuMapper.selectPageDto(new Page<>(currentPage, pageSize), wrapper);
        return songMenuDtoPage;
    }
}
