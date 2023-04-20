package com.example.musicbox.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "歌单组成", description = "歌曲与歌单多对多的组成关系")
@TableName("composition_song_menu_song")
public class SongMenuComposition {
    @ApiModelProperty("记录id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("歌单id")
    private Long songMenuId;

    @ApiModelProperty("歌曲ID")
    private Long songId;

    @ApiModelProperty("歌曲优先级")
    private Long songPriority;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
