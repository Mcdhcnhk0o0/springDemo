package com.example.springdemo.controller;


import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.dto.SunoClipDTO;
import com.example.springdemo.bean.dto.SunoGenerationRequest;
import com.example.springdemo.bean.dto.SunoGenerationDTO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.SunoService;
import com.example.springdemo.utils.UserInfoUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/suno")
@CrossOrigin
public class SunoController {

    @Resource
    private SunoService sunoService;

    @UserLoginToken
    @GetMapping("/submit/prompt")
    public Result<SunoGenerationDTO> submitPromptTask(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "prompt") String prompt
    ) {
        Long userId = UserInfoUtil.parseUserIdFromToken(token);
        return sunoService.generateByPrompt(userId, prompt, false);
    }

    @UserLoginToken
    @GetMapping("/acquire/prompt")
    public Result<SunoGenerationDTO> generateMusicByPrompt(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "prompt") String prompt
    ) {
        Long userId = UserInfoUtil.parseUserIdFromToken(token);
        return sunoService.generateByPrompt(0L, prompt, true);
    }

    @UserLoginToken
    @PostMapping("/submit/custom")
    public Result<SunoGenerationDTO> submitCustomTask(
            @RequestHeader(value = "token") String token,
            @RequestBody SunoGenerationRequest generationBean
    ) {
        Long userId = UserInfoUtil.parseUserIdFromToken(token);
        return sunoService.generateByCustom(userId, generationBean, false);
    }

    @UserLoginToken
    @PostMapping("/acquire/custom")
    public Result<SunoGenerationDTO> generateMusicByCustom(
            @RequestHeader(value = "token") String token,
            @RequestBody SunoGenerationRequest generationBean
    ) {
        Long userId = UserInfoUtil.parseUserIdFromToken(token);
        return sunoService.generateByCustom(userId, generationBean, true);
    }

    @UserLoginToken
    @GetMapping("/get/history")
    public Result<List<SunoClipDTO>> getGenerationHistory(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "pageNum") Integer pageNum
    ) {
        return sunoService.getGenerationHistory(pageNum);
    }

    @UserLoginToken
    @GetMapping("/get/feed")
    public Result<SunoClipDTO> getGenerationById(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "id") String id
    ) {
        return sunoService.getGenerationById(id);
    }

}
