package com.example.springdemo.bean.vo.protocol;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Result<T> {

    private Integer code = null;

    private String message = null;

    private T data;

    // 由于kotlin与lombok在编译时处于同一时期，因此kotlin无法正确获取lombok生成的方法
    // 故这里手动添加，避免data class与lombok混用时出现编译问题
    // todo: 抽空找一下更合理的解决办法
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result<T> success() {
        this.code = ResponseInfo.SUCCESS.getCode();
        this.message = ResponseInfo.SUCCESS.getMessage();
        return this;
    }

    public Result<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> success(T data) {
        this.data = data;
        this.code = ResponseInfo.SUCCESS.getCode();
        this.message = ResponseInfo.SUCCESS.getMessage();
        return this;
    }

    public Result<T> fail() {
        if (this.code != null) {
            this.code = ResponseInfo.FAIL.getCode();
        }
        if (this.message != null) {
            this.message = ResponseInfo.FAIL.getMessage();
        }
        return this;
    }

    public Result<T> fail(String info) {
        this.code = ResponseInfo.FAIL.getCode();
        if (info != null) {
            this.message = info;
        } else {
            this.message = ResponseInfo.FAIL.getMessage();
        }
        return this;
    }

}
