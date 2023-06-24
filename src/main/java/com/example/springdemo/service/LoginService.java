package com.example.springdemo.service;

import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.Result;
import com.example.springdemo.dao.User;

public interface LoginService {

    boolean signUp(User user);

    Result<LoginResult> login(User user);

    Result<LoginResult> logout(User user);
}
