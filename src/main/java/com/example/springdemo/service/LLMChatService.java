package com.example.springdemo.service;

import com.example.springdemo.bean.result.LLMResult;
import com.example.springdemo.bean.result.Result;
import okhttp3.WebSocket;

public interface LLMChatService {

    void startConnection(Long userId);

    Result<LLMResult> sendMessage(String message, Long userId);

}
