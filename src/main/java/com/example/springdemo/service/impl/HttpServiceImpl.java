package com.example.springdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.HttpService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class HttpServiceImpl implements HttpService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Result<String> get(String url) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", commonHeader());
        ResponseEntity<String> responseEntity;
        responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        return new Result<String>().success(responseEntity.getBody());
    }

    @Override
    public Result<String> post(String url, Map<String, Object> body) {
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(body), commonHeader());
        ResponseEntity<String> responseEntity;
        responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return new Result<String>().success(responseEntity.getBody());
    }

    @NotNull
    private HttpHeaders commonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
