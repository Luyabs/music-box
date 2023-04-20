package com.example.musicbox.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "用户聊天记录", description = "用户与用户多对多的聊天关系")
@TableName("chat_user")
public class UserChat {
    @ApiModelProperty("记录id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("发送者id")
    private Long senderId;

    @ApiModelProperty("接受者id")
    private Long receiverId;

    @ApiModelProperty("是否有管理员参与")
    private Boolean isAdministratorInvolved;

    @ApiModelProperty("发送内容")
    private String chatContent;

    @ApiModelProperty("聊天状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
