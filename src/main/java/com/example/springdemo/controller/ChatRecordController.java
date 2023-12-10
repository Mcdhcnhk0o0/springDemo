package com.example.springdemo.controller;

import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.dao.ChatRecord;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.ChatRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chat/record")
@CrossOrigin
public class ChatRecordController {

    @Resource
    private ChatRecordService chatRecordService;

    @UserLoginToken
    @GetMapping("/delete")
    public Result<Boolean> deleteChatRecord(
            @RequestParam(value = "chatId") Long chatId
    ) {
        return chatRecordService.deleteChatRecord(chatId);
    }

    @UserLoginToken
    @GetMapping("/get")
    public Result<List<ChatRecord>> getLatestChatRecords(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "pageNum") int pageNum
    ) {
        return chatRecordService.getLatestChatRecords(userId, pageSize, pageNum);
    }

}
