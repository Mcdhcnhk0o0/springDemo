package com.example.springdemo.bean.dto.juhe;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class NewsInfoDTO {

    @JsonAlias("uniquekey")
    private String uniqueKey;
    private String title;
    private String date;
    private String category;
    @JsonAlias("author_name")
    private String authorName;
    private String url;
    @JSONField(alternateNames = "thumbnail_pic_s")
    private String thumbnailPic;
    @JSONField(alternateNames = "thumbnail_pic_s02")
    private String thumbnailPic2;

}
