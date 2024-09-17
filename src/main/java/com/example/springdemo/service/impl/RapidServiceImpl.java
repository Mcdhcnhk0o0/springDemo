package com.example.springdemo.service.impl;


import com.example.springdemo.config.RapidConfig;
import com.example.springdemo.service.HttpService;
import com.example.springdemo.service.RapidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class RapidServiceImpl implements RapidService {

    @Resource
    private RapidConfig rapidConfig;

    @Resource
    private HttpService httpService;

    public String getOpenWeather(String city) {
        String url = String.format("https://open-weather13.p.rapidapi.com/city/%s/ZH_CN", city);
        String resp = httpService.get(url, weatherHeaders()).getData();
        log.info(resp);
        return resp;
    }

    private Map<String, Object> weatherHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-rapidapi-host", "open-weather13.p.rapidapi.com");
        headers.put("x-rapidapi-key", rapidConfig.getApiKey());
        return headers;
    }

}
