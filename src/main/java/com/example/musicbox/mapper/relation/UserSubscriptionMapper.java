package com.example.musicbox.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.musicbox.entity.User;
import com.example.musicbox.entity.relation.UserSubscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserSubscriptionMapper extends BaseMapper<UserSubscription> {
    /**
     * 按照id查询关注列表
     * @param userId 用户id
     * @return 分页返回关注列表*/
    @Select("""
            select * 
            from user u 
            where u.id in 
            (select followed_id 
            from subscription_user s 
            where s.follower_id=#{userId})
            order by s.update_time
            """)
    IPage<User> selectUserFollowed(@Param("userId") long userId, Page<User> page);


    /**
     * 按照id查询粉丝列表
     * @param userId 用户id
     * @return 分页返回粉丝列表
     * */
    @Select("""
            select *
            from user u
            where u.id in
            (select follower_id 
            from subscription_user s
            where s.followed_id=#{userId})
            order by s.update_time
            """)
    IPage<User> selectUserFollower(@Param("userId")long userId, Page<User> page);


}
