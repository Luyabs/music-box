use music_box;

create table abstract_user (
                               id bigint primary key ,
                               username varchar(80) unique ,
                               password varchar(80) not null

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

                      foreign key (id) references abstract_user(id)
);
create table creator(
    id bigint primary key ,
    creator_introduction varchar(500),
    stage_name varchar(20),
    Representative_work varchar(500),
    foreign key (id) references abstract_user(id)

);
create table songmenu(
    id bigint primary key ,
    status int default 0,
    menu_name varchar(50),
    menu_introduction varchar(200),
    is_album bool default false,
    create_user bigint,
    create_time datetime,

    foreign key (create_user) references abstract_user(id)
);
create table album(
    id bigint primary key ,
    album_introduction varchar(200),
    issue_company varchar(50),
    create_user bigint,
    create_time datetime,
    foreign key (id) references songmenu(id)
);
