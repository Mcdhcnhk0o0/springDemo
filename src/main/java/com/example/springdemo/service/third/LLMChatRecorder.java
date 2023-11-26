package com.example.springdemo.service.third;

import com.example.springdemo.bean.dto.LLMDTO.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LLMChatRecorder {

    public static LLMChatRecorder getInstance() {
        return InnerClass.instance;
    }

    private static class InnerClass {
        private static final LLMChatRecorder instance = new LLMChatRecorder();
    }

    private volatile boolean responseComplete = true;

    private final ConcurrentHashMap<Long, List<RoleContent>> recorder = new ConcurrentHashMap<>();

    public void record(Long userId, RoleContent content) {
        if (userId == null || content == null) {
            responseComplete = true;
            return;
        }
        if (recorder.containsKey(userId)) {
            List<RoleContent> contents = recorder.get(userId);
            if (contents.size() > 100) {
                contents.remove(0);
            }
            contents.add(content);
        } else {
            List<RoleContent> contents = new ArrayList<>();
            contents.add(content);
            recorder.put(userId, contents);
        }
        responseComplete = true;
    }

    public String getLatestReply(Long userId) {
        List<RoleContent> latestRecord = getLatestChatRecord(userId);
        if (latestRecord.size() > 0) {
            return latestRecord.get(latestRecord.size() - 1).getContent();
        }
        return "";
    }

    public List<RoleContent> getLatestChatRecord(Long userId) {
        List<RoleContent> chatRecords = new ArrayList<>();
        if (userId == null) {
            return chatRecords;
        }
        return recorder.getOrDefault(userId, chatRecords);
    }

    public void setResponseComplete(boolean responseComplete) {
        this.responseComplete = responseComplete;
    }

    public boolean isResponseComplete() {
        return responseComplete;
    }
}
