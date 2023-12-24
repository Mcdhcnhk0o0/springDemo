package com.example.springdemo.controller;

import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.vo.LLMVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.LLMChatService;
import com.example.springdemo.utils.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/llm")
@CrossOrigin
public class LLMController {

    @Resource
    private LLMChatService llmChatService;

    @UserLoginToken
    @GetMapping("/start")
    public Result<Boolean> startConnection(
            @RequestHeader(value = "token") String token
    ) {
        Long userId = getUserIdFromToken(token);
        return llmChatService.startConnection(userId);
    }

    @UserLoginToken
    @GetMapping("/send")
    public Result<LLMVO> sendMessage(
        @RequestHeader(value = "token") String token,
        @RequestParam(value = "message") String message
    ) {
        Long userId = getUserIdFromToken(token);
        return llmChatService.sendMessage(message, userId);
    }

    private Long getUserIdFromToken(String token) {
        String userIdStr = JWTUtil.Instance.getUserIdStrFromToken(token);
        return Long.parseLong(userIdStr);
    }

}
