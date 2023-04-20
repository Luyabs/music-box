package com.example.musicbox.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("帖子")
public class Post {
   @ApiModelProperty("帖子id")
   @TableId(type = IdType.ASSIGN_ID)
    private Long id;
   @ApiModelProperty("帖子创建者id")
    private Long user_id;
   @ApiModelProperty("帖子状态")
    private Integer status ;
   @ApiModelProperty("帖子主题")
    private Integer subject ;
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
