package com.example.springdemo.service;

import com.example.springdemo.bean.result.OcrResult;
import com.example.springdemo.bean.result.Result;

public interface OcrService {

    Result<OcrResult> getOcrResultWhenPicInUrl(String url);

}
