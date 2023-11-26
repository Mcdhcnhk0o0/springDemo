package com.example.springdemo.service;

import com.example.springdemo.bean.vo.LLMVO;
import com.example.springdemo.bean.vo.protocol.Result;

public interface LLMChatService {

    Result<Boolean> startConnection(Long userId);

    Result<LLMVO> sendMessage(String message, Long userId);

}
