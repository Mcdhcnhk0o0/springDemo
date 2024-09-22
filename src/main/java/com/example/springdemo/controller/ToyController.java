package com.example.springdemo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.nacos.NacosClient;
import com.example.springdemo.nacos.NacosTemplate;
import com.example.springdemo.utils.EncryptorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/toy")
@CrossOrigin
public class ToyController {

    @Resource
    private NacosTemplate nacosTemplate;

    @PassToken
    @GetMapping("/get_id_by_name")
    public JSONObject getIdByName(
            @RequestParam(value = "name", required = false) String name
    ) {
        if (name == null) {
            name = "unknown";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", String.valueOf(name.hashCode()));
        return jsonObject;
    }

    @PassToken
    @GetMapping("/get_coin_by_id")
    public JSONObject getCoinById(
            @RequestParam(value = "id", required = false) String id
    ) {
        if (id == null) {
            id = "99999996";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coin", String.valueOf(id.hashCode()));
        return jsonObject;
    }

    @PassToken
    @GetMapping("/config")
    public String getConfig(
            @RequestParam(value = "key") String key
    ) {
        return nacosTemplate.getConfig(key);
    }

}

