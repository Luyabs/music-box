package com.example.musicbox.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "用户歌曲播放记录", description = "用户与歌曲多对多的播放关系")
@TableName("playback_record_user_song")
public class SongPlayRecord {
    @ApiModelProperty("记录id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("歌曲id")
    private Long songId;

    @ApiModelProperty("行为记录 0: 播放  1: 下载  -1: 已逻辑删除此记录")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
