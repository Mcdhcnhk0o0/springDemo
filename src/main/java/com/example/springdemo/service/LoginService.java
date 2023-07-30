package com.example.springdemo.service;

import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.Result;


public interface LoginService {

    boolean signUp(String email, String userName, String password);

    Result<LoginResult> loginByEmail(String email, String password);

    Result<LoginResult> logout(Long userId, String email);
}
