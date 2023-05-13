package com.example.musicbox.dto;

import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.relation.PostReply;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "歌曲DTO", description = "歌曲 + 歌曲播放量")
public class SongDto extends Song {
    @ApiModelProperty("歌曲的播放量")
    private Long playVolume;

    @ApiModelProperty("歌曲的收藏量")
    private Long collection;
}
