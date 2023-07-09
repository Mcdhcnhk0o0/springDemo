package com.example.springdemo.service;

import com.example.springdemo.bean.OcrResult;
import com.example.springdemo.bean.Result;
import org.json.JSONObject;

public interface OcrService {

    Result<OcrResult> getOcrResultWhenPicInUrl(String url);

}
