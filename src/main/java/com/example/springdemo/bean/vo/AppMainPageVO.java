package com.example.springdemo.bean.vo;


import com.example.springdemo.bean.dto.juhe.FortuneInfoDTO;
import com.example.springdemo.bean.dto.juhe.NewsInfoDTO;
import com.example.springdemo.bean.dto.juhe.WeatherInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class AppMainPageVO {

    private WeatherInfoDTO weather;
    private FortuneInfoDTO fortune;
    private List<NewsInfoDTO> news;

}
