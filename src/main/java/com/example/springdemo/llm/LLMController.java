package com.example.springdemo.llm;


import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.vo.LLMVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.llm.protocol.Type;
import com.example.springdemo.utils.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/llm")
@CrossOrigin
public class LLMController {

    @Resource
    private LLMService llmService;

    @UserLoginToken
    @GetMapping("/send/sync")
    public Result<String> sendMessageSync(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "typeCode", required = false) Long typeCode,
            @RequestParam(value = "withContext", defaultValue = "false") Boolean withContext
    ) {
        Long userId = getUserIdFromToken(token);
        return llmService.sendMessageSync(userId, message, Type.fromCode(typeCode), withContext);
    }

    @UserLoginToken
    @GetMapping("/send/async")
    public Result<String> sendMessageAsync(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "typeCode", required = false) Long typeCode,
            @RequestParam(value = "withContext", defaultValue = "false") Boolean withContext
    ) {
        Long userId = getUserIdFromToken(token);
        return llmService.sendMessageAsync(userId, message, Type.fromCode(typeCode), withContext);
    }

    private Long getUserIdFromToken(String token) {
        String userIdStr = JWTUtil.Instance.getUserIdStrFromToken(token);
        return Long.parseLong(userIdStr);
    }

}
