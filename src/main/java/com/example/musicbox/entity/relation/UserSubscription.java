package com.example.musicbox.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "用户关注记录", description = "用户与用户多对多的关注关系")
@TableName("subscription_user")
public class UserSubscription {
    @ApiModelProperty("记录id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("关注者id")
    private Long followerId ;

    @ApiModelProperty("被关注者id")
    private Long followedId ;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
