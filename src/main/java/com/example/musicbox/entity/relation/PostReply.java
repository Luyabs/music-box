package com.example.musicbox.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "帖子回复记录", description = "帖子与用户多对多的回复关系")
@TableName("reply_user_post")
public class PostReply {
    @ApiModelProperty("记录id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("记录id")
    private Long postId;

    @ApiModelProperty("记录id")
    private Long replyUserId;

    @ApiModelProperty("记录id")
    private String commentsContent;

    @ApiModelProperty("记录id")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
