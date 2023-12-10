package com.example.springdemo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.bean.dao.ChatRecord;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.mapper.ChatRecordMapper;
import com.example.springdemo.service.ChatRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@DS("chat")
@Service
public class ChatRecordServiceImpl implements ChatRecordService {

    @Resource
    private ChatRecordMapper chatRecordMapper;

    @Override
    public Result<Boolean> addChatRecord(Long from, Long to, String message) {
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setFromWho(from);
        chatRecord.setToWho(to);
        chatRecord.setMessage(message);
        chatRecord.setGmtCreate(String.valueOf(System.currentTimeMillis()));
        chatRecordMapper.insert(chatRecord);
        return new Result<Boolean>().success(true);
    }

    @Override
    public Result<Boolean> deleteChatRecord(Long chatId) {
        UpdateWrapper<ChatRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("deleted", true);
        updateWrapper.eq("id", chatId);
        chatRecordMapper.update(null, updateWrapper);
        return new Result<Boolean>().success(true);
    }

    @Override
    public Result<Boolean> batchDeleteChatRecord(List<Long> chatIds) {
        return null;
    }

    @Override
    public Result<List<ChatRecord>> getLatestChatRecords(Long userId, int pageSize, int pageNum) {
        Wrapper<ChatRecord> queryWrapper = Wrappers.<ChatRecord>lambdaQuery()
                        .eq(ChatRecord::getFromWho, userId)
                        .or()
                        .eq(ChatRecord::getToWho, userId)
                        .orderByDesc(ChatRecord::getGmtCreate);
        Page<ChatRecord> page = new Page<>(pageNum, pageSize);
        List<ChatRecord> chatRecords = chatRecordMapper.selectPage(page, queryWrapper).getRecords();
        return new Result<List<ChatRecord>>().success(chatRecords);
    }

    @Override
    public Result<List<ChatRecord>> getChatRecordsByDate(Long userId, String date, int pageSize, int pageNum) {
        return null;
    }
}
