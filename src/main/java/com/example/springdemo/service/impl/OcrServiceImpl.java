package com.example.springdemo.service.impl;

import com.example.springdemo.bean.OcrResult;
import com.example.springdemo.bean.Result;
import com.example.springdemo.service.OcrService;
import com.example.springdemo.service.third.BaiduOcrService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class OcrServiceImpl implements OcrService {

    @Resource
    private BaiduOcrService baiduOcrService;

    @Override
    public Result<OcrResult> getOcrResultWhenPicInUrl(String url) {
        JSONObject resultJson = baiduOcrService.getOcrResultWhenPicInUrl(url);
        Result<OcrResult> result = new Result<>();
        OcrResult ocrResult = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ocrResult = objectMapper.readValue(resultJson.toString(2), OcrResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ocrResult != null) {
            result.setData(ocrResult);
            return result.success();
        } else {
            return result.fail();
        }
    }

}
