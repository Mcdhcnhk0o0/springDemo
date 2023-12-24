package com.example.springdemo.controller;

import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.dao.ChatRecord;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.ChatService;
import com.example.springdemo.utils.UserInfoUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin
public class ChatController {

    @Resource
    private ChatService chatRecordService;

    @UserLoginToken
    @PostMapping("/send")
    public Result<Boolean> send(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "targetUserId") Long targetUserId,
            @RequestParam(value = "message") String message
    ) {
        Long currentUserId = UserInfoUtil.parseUserIdFromToken(token);
        return chatRecordService.sendTo(currentUserId, targetUserId, message);
    }

    @UserLoginToken
    @GetMapping("/record/delete")
    public Result<Boolean> deleteChatRecord(
            @RequestParam(value = "chatId") Long chatId
    ) {
        return chatRecordService.deleteChatRecord(chatId);
    }

    @UserLoginToken
    @GetMapping("/record/get")
    public Result<List<ChatRecord>> getLatestChatRecords(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", required = false) int pageNum
    ) {
         return chatRecordService.getLatestChatRecords(userId, pageSize, pageNum);
    }

}
