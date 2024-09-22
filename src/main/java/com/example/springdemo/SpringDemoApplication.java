package com.example.springdemo;

import com.example.springdemo.nacos.NacosClient;
import com.example.springdemo.nacos.NacosConfig;
import com.example.springdemo.utils.EncryptorUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springdemo.mapper")
public class SpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
        System.out.println("==== Spring Demo start ====");
        System.out.println(System.currentTimeMillis());
        NacosClient.getInstance().initAndListen();
    }

}
