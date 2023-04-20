package com.example.musicbox;

import com.example.musicbox.mapper.*;
import com.example.musicbox.mapper.relation.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {
    @Autowired
    private AbstractUserMapper abstractUserMapper;
    @Autowired
    private AdministratorMapper administratorMapper;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private CreatorMapper creatorMapper;
    @Autowired
    private SongMenuMapper songMenuMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private SongCommentMapper songCommentMapper;
    @Autowired
    private SongMenuCompositionMapper songMenuCompositionMapper;
    @Autowired
    private SongPlayRecordMapper songPlayRecordMapper;
    @Autowired
    private UserMenuCollectionMapper userMenuCollectionMapper;
    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;
    @Autowired
    private PostReplyMapper postReplyMapper;
    @Autowired
    private UserChatMapper userChatMapper;


    @Test
    void mapperTest() {
//        abstractUserMapper.selectList(null);
//        albumMapper.selectList(null);
//        creatorMapper.selectList(null);
//        songMenuMapper.selectList(null);
//        userMapper.selectList(null);
//        administratorMapper.selectList(null);
//        postMapper.selectList(null);
//        songMapper.selectList(null);
        songCommentMapper.selectList(null);
        songMenuCompositionMapper.selectList(null);
        songPlayRecordMapper.selectList(null);
        userMenuCollectionMapper.selectList(null);
        userSubscriptionMapper.selectList(null);
        postReplyMapper.selectList(null);
        userChatMapper.selectList(null);
    }
}
