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
@ApiModel(value = "歌单", description = "专辑的父类")
public class SongMenu {
    @ApiModelProperty("歌单id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("歌单权限：-1：逻辑删除，0：公开 1: 私有")
    private Integer authority;

    @ApiModelProperty("歌单名")
    private String menuName;

    @ApiModelProperty("歌单简介")
    private String menuIntroduction;

    @ApiModelProperty("歌单封面")
    private String coverPicture;

    @ApiModelProperty("是否专辑")
    private Boolean isAlbum;

    @ApiModelProperty("建立者id")
    private Long userId;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
