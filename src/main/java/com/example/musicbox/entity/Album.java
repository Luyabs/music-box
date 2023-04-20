package com.example.musicbox.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "专辑", description = "歌单的子类")
public class Album {
    @ApiModelProperty("歌单id")
    @TableId
    private Long id;

    @ApiModelProperty("专辑简介")
    private String albumIntroduction;

    @ApiModelProperty("发行公司")
    private String issueCompany;

    @ApiModelProperty("发行时间")
    private LocalDateTime issueTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
