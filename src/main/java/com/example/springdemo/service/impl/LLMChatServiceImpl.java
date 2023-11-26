package com.example.springdemo.service.impl;

import com.example.springdemo.bean.vo.LLMVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.config.SparkLLMConfig;
import com.example.springdemo.service.LLMChatService;
import com.example.springdemo.service.third.LLMChatRecorder;
import com.example.springdemo.service.third.SparkLLMService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LLMChatServiceImpl implements LLMChatService {

    private static final int MAX_WAIT_TIME = 300;

    @Resource
    private SparkLLMService llmService;

    @Resource
    private SparkLLMConfig llmConfig;

    private WebSocket webSocket;

    @Override
    public Result<Boolean> startConnection(Long userId) {
        if (!LLMChatRecorder.getInstance().isResponseComplete()) {
            return new Result<Boolean>().success(false);
        }
        try {
            webSocket = llmService.startConnection();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<Boolean>().success(false);
        }
        return new Result<Boolean>().success(Boolean.TRUE);
    }

    @Override
    public Result<LLMVO> sendMessage(String message, Long userId) {
        if (webSocket == null || message == null || userId == null) {
            return new Result<LLMVO>().fail();
        }
        if (!LLMChatRecorder.getInstance().isResponseComplete()) {
            return new Result<LLMVO>().fail();
        }
        int before = LLMChatRecorder.getInstance().getLatestChatRecord(0L).size();
        try {
            webSocket = llmService.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int counter = 0;
        while (LLMChatRecorder.getInstance().getLatestChatRecord(0L).size() == before && counter < MAX_WAIT_TIME) {
            counter++;
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        webSocket.close(1000, "");
        return new Result<LLMVO>().success(getLatestReply());
    }

    private LLMVO getLatestReply() {
        LLMVO result = new LLMVO();
        String ans = LLMChatRecorder.getInstance().getLatestReply(0L);
        List<String> ansList = new ArrayList<>();
        ansList.add(ans);
        result.setDisplayText(ansList);
        return result;
    }

}
