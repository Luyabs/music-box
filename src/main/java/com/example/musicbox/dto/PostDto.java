package com.example.musicbox.dto;

import com.example.musicbox.entity.Post;
import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.SongMenu;
import com.example.musicbox.entity.relation.PostReply;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "帖子dto", description = "帖子 + 帖子中的回复dto")
public class PostDto extends Post {
    @ApiModelProperty("帖子中的回复")
    private List<PostReply> postReplyList;
}
