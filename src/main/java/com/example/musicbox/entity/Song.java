package com.example.musicbox.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel(value = "歌曲")
public class Song {
    @ApiModelProperty("歌曲id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("创建者id")
    private Long user_id;
    @ApiModelProperty("歌曲封面")
    private String cover_picture;
    @ApiModelProperty("歌曲名")
    private String singer_name;
    @ApiModelProperty("歌手名")
    private String song_name;
    @ApiModelProperty("歌曲状态")
    private Integer status;
    @ApiModelProperty("歌曲发布时间")
    private LocalDateTime issue_time;
    @ApiModelProperty("歌曲语言")
    private String language;
    @ApiModelProperty("歌曲分类")
    private String classification;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
