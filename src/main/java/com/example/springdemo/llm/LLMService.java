package com.example.springdemo.llm;

import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.llm.protocol.Type;

public interface LLMService {

    Result<String> sendMessageSync(Long userId, String message, Type type, boolean withContext);

    Result<String> sendMessageAsync(Long userId, String message, Type type, boolean withContext);

    Result<Boolean> clearContext(Long userId);

}
