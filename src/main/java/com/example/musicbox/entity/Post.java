package com.example.musicbox.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel("帖子")
public class Post {
   @ApiModelProperty("帖子id")
   @TableId(type = IdType.ASSIGN_ID)
    private Long id;

   @ApiModelProperty("帖子创建者id")
    private Long userId;

    @ApiModelProperty("帖子内容")
    private String content;

   @ApiModelProperty("帖子状态：" +
           "-1为被删/异常：0，1,2分别代表所有人可见，部分人可见，仅自己可见")
    private Integer status ;

   @ApiModelProperty("帖子主题")
    private String subject ;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
