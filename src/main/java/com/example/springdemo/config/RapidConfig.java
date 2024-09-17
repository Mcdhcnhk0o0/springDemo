package com.example.springdemo.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.custom.rapid.weather")
public class RapidConfig {

    private String apiKey;

}
