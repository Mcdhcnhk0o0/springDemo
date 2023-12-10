package com.example.springdemo.service;

import com.example.springdemo.bean.dao.ChatRecord;
import com.example.springdemo.bean.vo.protocol.Result;

import java.util.List;

public interface ChatRecordService {

    Result<Boolean> addChatRecord(Long from, Long to, String message);

    Result<Boolean> deleteChatRecord(Long chatId);

    Result<Boolean> batchDeleteChatRecord(List<Long> chatIds);

    Result<List<ChatRecord>> getLatestChatRecords(Long userId, int pageSize, int pageNum);

    Result<List<ChatRecord>> getChatRecordsByDate(Long userId, String date, int pageSize, int pageNum);

}
