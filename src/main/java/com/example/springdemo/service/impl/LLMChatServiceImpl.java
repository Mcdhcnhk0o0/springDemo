package com.example.springdemo.service.impl;

import com.example.springdemo.bean.result.LLMResult;
import com.example.springdemo.bean.result.Result;
import com.example.springdemo.config.SparkLLMConfig;
import com.example.springdemo.service.LLMChatService;
import com.example.springdemo.service.third.LLMChatRecorder;
import com.example.springdemo.service.third.SparkLLMService;
import com.example.springdemo.utils.BigModelNew;
import com.example.springdemo.utils.SparkLLMUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LLMChatServiceImpl implements LLMChatService {

    @Resource
    private SparkLLMService llmService;

    @Resource
    private BigModelNew bigModelNew;

    @Resource
    private SparkLLMConfig llmConfig;

    private WebSocket webSocket;

    @Override
    public void startConnection(Long userId) {
        try {
            webSocket = bigModelNew.start("你好");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
//        webSocket = llmService.startConnection();
//        String request = SparkLLMUtil.getAuthRequest(llmConfig.getAppId(), "Hello~", new ArrayList<>());
//        if (webSocket != null) {
//            webSocket.send(request);
//        }
    }

    @Override
    public Result<LLMResult> sendMessage(String message, Long userId) {
        if (webSocket == null || message == null || userId == null) {
            return new Result<LLMResult>().success();
        }
        int before = LLMChatRecorder.getInstance().getLatestChatRecord(0L).size();
        try {
            webSocket = bigModelNew.start(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (LLMChatRecorder.getInstance().getLatestChatRecord(0L).size() == before) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LLMResult result = new LLMResult();
        String ans = LLMChatRecorder.getInstance().getLatestChatRecord(0L).get(before).getContent();
        List<String> ansList = new ArrayList<>();
        ansList.add(ans);
        result.setDisplayText(ansList);
        return new Result<LLMResult>().success(result);
    }
}
