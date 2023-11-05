package com.example.springdemo.factory;

import com.example.springdemo.bean.result.LoginResult;
import com.example.springdemo.bean.result.Result;
import com.example.springdemo.dao.User;
import com.example.springdemo.utils.ResponseInfo;

public class LoginResultBuilder {

    private final LoginResult loginResult = new LoginResult();

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

    public Result<LoginResult> fail() {
        loginResult.setSuccess(false);
        Result<LoginResult> result = new Result<>();
        result.setCode(ResponseInfo.SUCCESS.getCode());
        result.setMessage(ResponseInfo.SUCCESS.getMessage());
        result.setData(loginResult);
        return result;
    }

    public Result<LoginResult> success() {
        loginResult.setSuccess(true);
        Result<LoginResult> result = new Result<>();
        result.setCode(ResponseInfo.SUCCESS.getCode());
        result.setMessage(ResponseInfo.SUCCESS.getMessage());
        result.setData(loginResult);
        return result;
    }

}
