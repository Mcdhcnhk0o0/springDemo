package com.example.springdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.custom.xf.spark")
public class SparkLLMConfig {

    private String appId;
    private String apiKey;
    private String apiSecret;

}
