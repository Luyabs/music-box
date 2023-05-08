package com.example.musicbox.dto;

import com.example.musicbox.entity.Song;
import com.example.musicbox.entity.SongMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "歌单dto", description = "歌单 + 歌单中的歌曲dto")
public class SongMenuDto extends SongMenu {
    @ApiModelProperty("歌单中的歌曲")
    private List<Song> songList;
}
