package com.example.springdemo.service.impl;

import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.HttpService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class HttpServiceImpl implements HttpService {

    @Resource
    private OkHttpClient okHttpClient;

    @Override
    public Result<String> get(String url) {
        Request.Builder requestBuilder = new Request.Builder().get();
//        okHttpClient.newCall();
        return null;
    }

    @Override
    public Result<String> post(String url) {
        return null;
    }
}
