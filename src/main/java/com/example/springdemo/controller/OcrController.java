package com.example.springdemo.controller;

import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.result.OcrResult;
import com.example.springdemo.bean.result.Result;
import com.example.springdemo.service.OcrService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ocr")
@CrossOrigin
public class OcrController {

    @Resource
    private OcrService ocrService;

    @UserLoginToken
    @GetMapping("/getByPicUrl")
    public Result<OcrResult> getOcrResultWhenPicInUrl(
            @RequestParam(value = "url") String url
    ) {
        return ocrService.getOcrResultWhenPicInUrl(url);
    }

}
