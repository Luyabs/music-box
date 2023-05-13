package com.example.musicbox.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "专辑dto", description = "歌单 + 专辑dto + 被收藏量")
public class AlbumDtowithCollection extends AlbumDto{
    @ApiModelProperty("被收藏次数")
    private Long collectionNumber;
}

