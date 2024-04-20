package com.example.springdemo.service;

import com.example.springdemo.bean.vo.protocol.Result;

import java.util.Map;

public interface HttpService {

    Result<String> get(String url);

    Result<String> post(String url, Map<String, Object> body);

}
