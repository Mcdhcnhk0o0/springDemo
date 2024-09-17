package com.example.springdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.springdemo.bean.NewsType;
import com.example.springdemo.bean.dto.juhe.FortuneInfoDTO;
import com.example.springdemo.bean.dto.juhe.NewsInfoDTO;
import com.example.springdemo.bean.dto.juhe.WeatherInfoDTO;
import com.example.springdemo.service.JuheService;
import com.example.springdemo.service.helper.JuheServiceHelper;
import com.example.springdemo.service.third.response.JuheNewsResponse;
import com.example.springdemo.service.third.response.JuheWeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class JuheServiceImpl implements JuheService {

    @Resource
    private JuheServiceHelper juheServiceHelper;

    public List<NewsInfoDTO> fetchTopNews(NewsType type, Integer pageNum, Integer pageSize) {
        String httpResponse = juheServiceHelper.fetchNews(type, pageNum, pageSize);
        JuheNewsResponse juheNewsResponse = JSON.parseObject(httpResponse, JuheNewsResponse.class);
        log.info(httpResponse);
        return juheNewsResponse.getResult().getData();
    }

    public WeatherInfoDTO getWeather(String cityInChinese) {
        String httpResponse = juheServiceHelper.getWeather(cityInChinese);
        JuheWeatherResponse juheWeatherResponse = JSON.parseObject(httpResponse, JuheWeatherResponse.class);
        return juheWeatherResponse.getResult();
    }

    public FortuneInfoDTO getFortune(String consInChinese) {
        String httpResponse = juheServiceHelper.getFortune(consInChinese);
        FortuneInfoDTO fortuneInfoDTO = JSON.parseObject(httpResponse, FortuneInfoDTO.class);
        return fortuneInfoDTO;
    }

}
