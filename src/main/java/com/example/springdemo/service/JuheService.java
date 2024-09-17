package com.example.springdemo.service;

import com.example.springdemo.bean.NewsType;
import com.example.springdemo.bean.dto.juhe.FortuneInfoDTO;
import com.example.springdemo.bean.dto.juhe.NewsInfoDTO;
import com.example.springdemo.bean.dto.juhe.WeatherInfoDTO;

import java.util.List;


public interface JuheService {

    List<NewsInfoDTO> fetchTopNews(NewsType type, Integer pageNum, Integer pageSize);

    WeatherInfoDTO getWeather(String cityName);

    FortuneInfoDTO getFortune(String consName);

}
