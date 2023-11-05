package com.example.springdemo.service.third;

import com.example.springdemo.bean.response.LLMResponse.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LLMChatRecorder {

    public static LLMChatRecorder getInstance() {
        return InnerClass.instance;
    }

    private static class InnerClass {
        private static final LLMChatRecorder instance = new LLMChatRecorder();
    }

    private final ConcurrentHashMap<Long, List<RoleContent>> recorder = new ConcurrentHashMap<>();

    public void record(Long userId, RoleContent content) {
        if (userId == null || content == null) {
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
    }

    public List<RoleContent> getLatestChatRecord(Long userId) {
        List<RoleContent> chatRecords = new ArrayList<>();
        if (userId == null) {
            return chatRecords;
        }
        return recorder.getOrDefault(userId, chatRecords);
    }
}
