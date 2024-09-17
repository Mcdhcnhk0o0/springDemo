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
        return get(url, null);
    }

    @Override
    public Result<String> get(String url, Map<String, Object> headers) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", getHeaders(headers));
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
        return post(url, body, null);
    }

    @Override
    public Result<String> post(String url, Map<String, Object> body, Map<String, Object> headers) {
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(body), getHeaders(headers));
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
    private HttpHeaders getHeaders(Map<String, Object> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers == null || headers.isEmpty()) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return httpHeaders;
        }
        for (Map.Entry<String, Object> entry: headers.entrySet()) {
            httpHeaders.set(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return httpHeaders;
    }

}
