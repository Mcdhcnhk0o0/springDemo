package com.example.springdemo.controller;

import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.result.LLMResult;
import com.example.springdemo.bean.result.Result;
import com.example.springdemo.service.LLMChatService;
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
    public Result<Boolean> startConnection() {
        return llmChatService.startConnection(0L);
    }

    @UserLoginToken
    @GetMapping("/send")
    public Result<LLMResult> sendMessage(
        @RequestParam(value = "message") String message
    ) {
        return llmChatService.sendMessage(message, 0L);
    }

}
