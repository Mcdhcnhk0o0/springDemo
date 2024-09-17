package com.example.springdemo.bean.dto.juhe;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WeatherInfoDTO {

    @Data
    public static class Realtime {
        private String temperature;
        private String humidity;
        private String info;
        private String wid;
        private String direct;
        private String power;
        private String aqi;
    }

    @Data
    public static class Daily {
        private String date;
        private String temperature;
        private String weather;
        private String direct;
        private Map<String, Object> wid;
    }

    private String city;
    private Realtime realtime;
    private List<Daily> future;

}
