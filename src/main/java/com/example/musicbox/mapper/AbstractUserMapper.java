package com.example.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.musicbox.entity.AbstractUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AbstractUserMapper extends BaseMapper<AbstractUser> {
}
