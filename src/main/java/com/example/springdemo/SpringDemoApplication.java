package com.example.springdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springdemo.mapper")
public class SpringDemoApplication {

    public static void main(String[] args) {
        // *y;:84@HLGC)UYz
        SpringApplication.run(SpringDemoApplication.class, args);
        System.out.println("==== Spring Demo start ====");
        System.out.println(System.currentTimeMillis());
    }

}
