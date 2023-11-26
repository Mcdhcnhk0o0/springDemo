package com.example.springdemo.service;

import com.example.springdemo.bean.vo.OcrVO;
import com.example.springdemo.bean.vo.protocol.Result;

public interface OcrService {

    Result<OcrVO> getOcrResultWhenPicInUrl(String url);

}
