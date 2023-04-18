package com.example.musicbox.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "普通用户", description = "继承自abstract_user")
public class User {
    @ApiModelProperty("用户id")
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

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("是否为创作者")
    private Boolean isCreator;

    @ApiModelProperty("是否为VIP")
    private Boolean isVip;
}
