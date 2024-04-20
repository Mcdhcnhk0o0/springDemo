package com.example.springdemo.bean.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.Map;

@Data
public class SunoClipDTO {

    private String id;
    @JsonAlias("video_url")
    private String videoUrl;
    @JsonAlias("audio_url")
    private String audioUrl;
    @JsonAlias("image_url")
    private String imageUrl;
    @JsonAlias("image_large_url")
    private String imageLargeUrl;
    private String status;
    private String title;
    private Map<String, Object> metadata;

}
