package com.example.springdemo.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.custom.baidu.ocr")
public class BaiduOcrConfig {

    private String appId;
    private String apiKey;
    private String apiSecret;

}
