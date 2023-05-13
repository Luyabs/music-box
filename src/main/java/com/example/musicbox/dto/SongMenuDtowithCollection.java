package com.example.musicbox.dto;

import com.example.musicbox.entity.Song;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "歌单dto", description = "歌单 + 歌单被收藏次数 +（歌单下歌曲）")
public class SongMenuDtowithCollection extends SongMenuDto {
    @ApiModelProperty("被收藏次数")
    private Long collectionNumber;
}
