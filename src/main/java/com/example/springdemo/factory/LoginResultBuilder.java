package com.example.springdemo.factory;

import com.example.springdemo.bean.vo.LoginVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.bean.dao.User;
import com.example.springdemo.bean.vo.protocol.ResponseInfo;

public class LoginResultBuilder {

    private final LoginVO loginResult = new LoginVO();

    public LoginResultBuilder setUser(User user) {
        this.loginResult.setUser(user);
        return this;
    }

    public LoginResultBuilder setToken(String token) {
        this.loginResult.setToken(token);
        return this;
    }

    public LoginResultBuilder setStatusCode(Integer statusCode) {
        this.loginResult.setStatusCode(statusCode);
        return this;
    }

    public LoginResultBuilder setStatusMessage(String statusMessage) {
        this.loginResult.setStatusMessage(statusMessage);
        return this;
    }

    public LoginResultBuilder setLatestLoginTime(String latestLoginTime) {
        this.loginResult.setLatestLoginTime(latestLoginTime);
        return this;
    }

    public Result<LoginVO> fail() {
        loginResult.setSuccess(false);
        Result<LoginVO> result = new Result<>();
        result.setCode(ResponseInfo.SUCCESS.getCode());
        result.setMessage(ResponseInfo.SUCCESS.getMessage());
        result.setData(loginResult);
        return result;
    }

    public Result<LoginVO> success() {
        loginResult.setSuccess(true);
        Result<LoginVO> result = new Result<>();
        result.setCode(ResponseInfo.SUCCESS.getCode());
        result.setMessage(ResponseInfo.SUCCESS.getMessage());
        result.setData(loginResult);
        return result;
    }

}
