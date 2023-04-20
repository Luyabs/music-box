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
@ApiModel(value = "普通用户", description = "继承自abstract_user")
public class User {
    @ApiModelProperty("用户id")
    @TableId
    private Long id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("地区")
    private String region;

    @ApiModelProperty("个性签名")
    private String signature;

    @ApiModelProperty("职业")
    private String profession;

    @ApiModelProperty("本地下载目录")
    private String localDownloadingDirectory;

    @ApiModelProperty("状态 0: 正常 1: 封禁")
    private Integer status;

    @ApiModelProperty("是否为创作者")
    private Boolean isCreator;

    @ApiModelProperty("是否为VIP")
    private Boolean isVip;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
