package com.example.springdemo.service;

import com.example.springdemo.bean.vo.protocol.Result;

public interface HttpService {

    Result<String> get(String url);

    Result<String> post(String url);

}
