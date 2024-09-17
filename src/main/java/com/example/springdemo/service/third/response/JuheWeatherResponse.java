package com.example.springdemo.service.third.response;

import com.example.springdemo.bean.dto.juhe.WeatherInfoDTO;
import lombok.Data;

@Data
public class JuheWeatherResponse {

    private String reason;
    private WeatherInfoDTO result;

}
