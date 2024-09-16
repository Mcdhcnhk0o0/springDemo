package com.example.springdemo.controller;


import com.alibaba.fastjson.JSON;
import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/hook")
@CrossOrigin
public class WebhookController {

    @Resource
    private WebSocketService webSocketService;

    @PassToken
    @PostMapping("/transfer")
    public void postTransfer(@RequestBody Object body) {
        String bodyStr = JSON.toJSONString(body);
        log.info("received message: " + bodyStr);
        webSocketService.broadcastToAll(bodyStr);
    }

}

