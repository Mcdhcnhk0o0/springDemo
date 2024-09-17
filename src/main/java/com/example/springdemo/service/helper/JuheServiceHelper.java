package com.example.springdemo.service.helper;

import com.example.springdemo.bean.NewsType;
import com.example.springdemo.config.JuheConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JuheServiceHelper {

    @Resource
    private JuheConfig juheConfig;

    private static final String newsApiUrl = "http://v.juhe.cn/toutiao/index";
    private static final String weatherApiUrl = "http://apis.juhe.cn/simpleWeather/query";
    private static final String fortuneApiUrl = "http://web.juhe.cn/constellation/getAll";

    public String fetchNews(NewsType type, Integer pageNum, Integer pageSize) {
        String newsType = type == null ? NewsType.TOP.getKey() : type.getKey();
        int curPageNum = pageNum == null ? 1 : pageNum;
        int curPageSize = pageSize == null ? 30 : pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("key", juheConfig.getNewsApiKey());
        map.put("type", newsType);
        map.put("page", curPageNum);
        map.put("page_size", curPageSize);
        map.put("is_filter", 1);
        return urlGet(newsApiUrl, map);
    }

    public String getWeather(String cityInChinese) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", juheConfig.getWeatherApiKey());
        map.put("city", cityInChinese);
        return urlGet(weatherApiUrl, map);
    }

    public String getFortune(String consNameInChinese) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", juheConfig.getFortuneApiKey());
        try {
            map.put("consName", URLEncoder.encode(consNameInChinese, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("type", "today");
        return urlGet(fortuneApiUrl, map);
    }

    private static String urlGet(String url, Map<String, Object> params) {
        BufferedReader in = null;
        StringBuilder response = new StringBuilder();
        try {
            URL targetUrl = new URL(url + "?" + paramsToString(params));
            in = new BufferedReader(new InputStreamReader((targetUrl.openConnection()).getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }

    private static String paramsToString(Map<String, Object> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

}
