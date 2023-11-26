package com.example.springdemo.controller;

import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.vo.OcrVO;
import com.example.springdemo.bean.vo.protocol.Result;
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
    public Result<OcrVO> getOcrResultWhenPicInUrl(
            @RequestParam(value = "url") String url
    ) {
        return ocrService.getOcrResultWhenPicInUrl(url);
    }

}
