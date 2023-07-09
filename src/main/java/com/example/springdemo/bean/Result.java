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

    public Result<T> success() {
        this.code = ResponseInfo.SUCCESS.getCode();
        this.message = ResponseInfo.SUCCESS.getMessage();
        return this;
    }

    public Result<T> fail() {
        this.code = ResponseInfo.FAIL.getCode();
        this.message = ResponseInfo.FAIL.getMessage();
        return this;
    }

}
