package com.example.musicbox.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "歌曲")
public class Song {
    @ApiModelProperty("歌曲id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("创建者id")
    private Long userId;

    @ApiModelProperty("是否只有vip才能下载/播放")
    private Boolean isVip;

    @ApiModelProperty("文件所在目录")
    private String fileDirectory;

    @ApiModelProperty("歌曲封面")
    private String coverPicture;

    @ApiModelProperty("歌曲名")
    private String singerName;

    @ApiModelProperty("歌手名")
    private String songName;

    @ApiModelProperty("歌曲状态 0: 正常 暂时不设置其他状态")
    private Integer status;

    @ApiModelProperty("歌曲发布时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueTime;

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
