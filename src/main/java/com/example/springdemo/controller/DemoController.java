package com.example.springdemo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class DemoController {

    @GetMapping(value = "/hello", name = "问好")
    public String hello(@RequestParam(value = "name", required = false) String name) {
        return String.format("Hello, %s", name == null ? "World" : name);
    }

    @GetMapping(value = "/user")
    public Map<String, String> getUser(@RequestParam(value = "id") String id) {
        Map<String, String> info = new HashMap<>();
        info.put("name", "wenqiang");
        info.put("id", id == null ? "unknown" : id);
        return info;
    }

}
