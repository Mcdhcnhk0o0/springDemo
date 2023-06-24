package com.example.springdemo.bean;

import com.example.springdemo.utils.ResponseInfo;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Result<T> {

    private Integer code = ResponseInfo.DEFAULT.getCode();

    private String message = ResponseInfo.DEFAULT.getMessage();

    private T data;

}
