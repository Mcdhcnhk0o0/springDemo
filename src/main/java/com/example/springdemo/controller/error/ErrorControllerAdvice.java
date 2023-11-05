package com.example.springdemo.controller.error;

import com.example.springdemo.bean.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> handleException(Exception ex, HandlerMethod hm) {
        Result<String> result = new Result<>();
        result.setCode(400);
        result.setMessage(ex.getMessage());
        result.setData(hm.getBean().getClass() + "#" + hm.getMethod().getName());
        return result;
    }

}
