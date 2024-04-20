package com.example.springdemo.controller;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/mq")
public class RabbitMqController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("send/topic")
    public String sendTopicMessage(
        @RequestParam(value = "message") String message
    ) {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: first";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> firstMap = new HashMap<>();
        firstMap.put("messageId", messageId);
        firstMap.put("messageData", messageData);
        firstMap.put("createTime", createTime);
        rabbitTemplate.convertAndSend("topicExchange", "topic.first", firstMap);
        return "ok";
    }

}
