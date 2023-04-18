package com.example.musicbox.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "创作者", description = "继承自user")
public class Creator {
    @ApiModelProperty("用户id")
    @TableId
    private Long id;

    @ApiModelProperty("是否为创作者")
    private String creatorIntroduction;

    @ApiModelProperty("艺名/曾用名")
    private String stageName;

    @ApiModelProperty("代表作")
    private String representativeWork;

    @ApiModelProperty("演艺经历")
    private String performingExperience;

    @ApiModelProperty("主要成就")
    private String majorAchievement;

    @ApiModelProperty("经纪公司")
    private String brokerageCompany;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
