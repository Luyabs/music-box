package com.example.musicbox.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "歌曲评论", description = "歌曲与用户多对多的评论关系")
@TableName("comment_user_song")
public class SongComment {
    @ApiModelProperty("记录id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("歌曲id")
    private Long songId;

    @ApiModelProperty("评论内容")
    private String commentsContent;

    @ApiModelProperty("评论状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
