use music_box;

create table abstract_user (
    id bigint primary key ,
    username varchar(80) unique ,
    password varchar(80) not null,
    create_time datetime,
    update_time datetime
);

create table user (
    id bigint primary key ,
    nickname varchar(80) unique ,
    avatar varchar(120) ,
    gender varchar(5) check ( gender in ('f', 'm', 'none') ),
    region varchar(50) ,
    signature varchar(80) ,
    profession varchar(80) ,
    local_downloading_directory varchar(120),
    status int default 0,
    is_creator bool default false,
    is_vip bool default false,
    create_time datetime,
    update_time datetime,
    foreign key (id) references abstract_user(id)
);
create table creator(
    id bigint primary key ,                 #创作者id
    creator_introduction varchar(500),      #创作者简介
    stage_name varchar(20),                 #艺名/曾用名
    representative_work varchar(500),       #代表作
    performing_experience varchar(500),     #演艺经历
    major_achievement varchar(500),         #主要成就
    brokerage_company varchar(50),          #经纪公司
    create_time datetime,
    update_time datetime,
    foreign key (id) references abstract_user(id)

);
create table administrator(
    id bigint primary key ,                 #管理员id
    authority_level int default 0,          #管理员权限等级
    create_time datetime,
    update_time datetime,
    foreign key (id) references abstract_user(id)

);
create table song(
    id bigint primary key ,                 #歌曲id
    user_id bigint,                         #建立者id
    cover_picture varchar(200),             #创作者id
    file_directory varchar(200),            #本地保存路径
    singer_name varchar(50),                #歌手名
    song_name varchar(50),                  #歌曲名
    status int default 0,                  #审核状态
    issue_time datetime,                    #发行时间
    language varchar(50),                   #语言
    classification varchar(50),             #分类
    create_time datetime,
    update_time datetime,
    foreign key (user_id) references abstract_user(id)
);
create table song_menu(
    id bigint primary key ,                 #歌单id
    status int default 0,                   #歌单状态
    menu_name varchar(50),                  #歌单名
    menu_introduction varchar(200),         #歌单简介
    is_album bool default false,            #是否专辑
    user_id bigint,                         #建立者id
    create_time datetime,
    update_time datetime,
    foreign key (user_id) references abstract_user(id)
);
create table album(
    id bigint primary key ,                 #专辑id
    album_introduction varchar(200),        #专辑简介
    issue_company varchar(50),              #发行公司
    issue_time datetime,                    #发行时间
    create_time datetime,
    update_time datetime,
    foreign key (id) references song_menu(id)
);
create table post(
    id bigint primary key ,                 #帖子id
    user_id bigint,                         #建立者id
    status int default 0,                   #帖子状态
    subject varchar(50),                    #帖子主题
    create_time datetime,
    update_time datetime,
    foreign key (user_id) references abstract_user(id)
);
#用户收藏歌单
create table collection_user_song_menu(
    id bigint primary key ,                 #id
    user_id bigint,                         #用户ID
    song_menu_id bigint,                    #歌单id
    create_time datetime,
    update_time datetime,
    foreign key (user_id) references abstract_user(id),
    foreign key (song_menu_id) references song_menu(id)
);
#歌曲组成歌单
create table composition_song_menu_song(
    id bigint primary key ,                 #id
    song_menu_id bigint,                    #歌单id
    song_id bigint,                         #歌曲ID
    song_priority bigint,                   #歌曲优先级
    create_time datetime,
    update_time datetime,
    foreign key (song_menu_id) references song_menu (id),
    foreign key (song_id) references song (id)
);
create table comment_user_song(
    id bigint primary key ,                 #id
    user_id bigint,                         #用户ID
    song_id bigint,                         #歌曲id
    comments_content varchar(500),          #评论内容
    status int default 0,                   #评论状态
    create_time datetime,
    update_time datetime,
    foreign key (user_id) references abstract_user(id),
    foreign key (song_id) references song (id)
);
create table playback_record_user_song(
    id bigint primary key ,                 #id
    user_id bigint,                         #用户ID
    song_id bigint,                         #歌曲id
    status int default 0,                   #播放状态
    create_time datetime,
    update_time datetime,
    foreign key (user_id) references abstract_user(id),
    foreign key (song_id) references song (id)
);

create table subscription_user(
    id bigint primary key ,                 #id
    follower_id bigint,                     #关注者id
    followed_id bigint,                     #被关注者id
    create_time datetime,
    update_time datetime,
    foreign key (follower_id) references abstract_user(id),
    foreign key (followed_id) references abstract_user(id)
);
create table reply_user_post(
    id bigint primary key ,                 #id
    post_id bigint,                         #帖子id
    reply_user_id bigint,                   #回复用户ID
    comments_content varchar(500),          #回复内容
    status int default 0,                   #回复状态
    create_time datetime,
    update_time datetime,
    foreign key (reply_user_id) references abstract_user(id),
    foreign key (post_id) references post(id)
);
create table chat_abstract_user(
    id bigint primary key ,                 #id
    sender_id bigint,                       #发送者id
    receiver_id bigint,                     #接受者id
    is_administrator_involved bool default false,   #是否有管理员参与
    chat_content varchar(200),              #发送内容
    status int default 0,                   #聊天状态
    create_time datetime,
    update_time datetime,
    foreign key (sender_id) references abstract_user(id),
    foreign key (receiver_id) references abstract_user(id)
);

