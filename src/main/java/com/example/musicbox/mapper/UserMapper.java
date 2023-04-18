package com.example.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
