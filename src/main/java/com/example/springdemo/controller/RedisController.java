package com.example.springdemo.controller;


import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.vo.protocol.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/redis")
@CrossOrigin
public class RedisController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @UserLoginToken
    @GetMapping("/set")
    public void setConfig(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "value") Object value
    ) {
        redisTemplate.opsForValue().set(key, value);
    }

    @PassToken
    @GetMapping("/get")
    public Result<String> getConfig(
            @RequestParam(value = "key") String key
    ) {
        Object value = redisTemplate.opsForValue().get(key);
        return new Result<String>().success(String.valueOf(value));
    }

}

