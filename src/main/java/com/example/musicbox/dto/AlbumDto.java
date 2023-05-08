package com.example.musicbox.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "专辑dto", description = "歌单 + 专辑dto")
public class AlbumDto extends SongMenuDto {
    @ApiModelProperty("专辑简介")
    private String albumIntroduction;

    @ApiModelProperty("发行公司")
    private String issueCompany;

    @ApiModelProperty("发行时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueTime;
}
