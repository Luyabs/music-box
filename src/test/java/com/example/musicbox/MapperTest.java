package com.example.musicbox;

import com.example.musicbox.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {
    @Autowired
    private AbstractUserMapper abstractUserMapper;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private CreatorMapper creatorMapper;
    @Autowired
    private SongMenuMapper songMenuMapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    void mapperTest() {
        abstractUserMapper.selectList(null);
        albumMapper.selectList(null);
        creatorMapper.selectList(null);
        songMenuMapper.selectList(null);
        userMapper.selectList(null);
    }
}
