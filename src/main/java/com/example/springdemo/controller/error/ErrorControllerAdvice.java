package com.example.springdemo.controller.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler
    void handleException(RuntimeException ex, HandlerMethod hm) {
        System.out.println("统一异常处理");
        System.out.println(ex.getMessage());
        System.out.println(hm.getBean().getClass());
        System.out.println(hm.getMethod().getName());
    }

}
