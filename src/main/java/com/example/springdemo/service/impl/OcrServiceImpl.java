package com.example.springdemo.service.impl;

import com.example.springdemo.bean.vo.OcrVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.OcrService;
import com.example.springdemo.service.third.BaiduOcrService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class OcrServiceImpl implements OcrService {

    @Resource
    private BaiduOcrService baiduOcrService;

    @Override
    public Result<OcrVO> getOcrResultWhenPicInUrl(String url) {
        JSONObject resultJson = baiduOcrService.getOcrResultWhenPicInUrl(url);
        Result<OcrVO> result = new Result<>();
        OcrVO ocrResult = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ocrResult = objectMapper.readValue(resultJson.toString(2), OcrVO.class);
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
