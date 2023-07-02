package com.example.springdemo.service;

import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.Result;
import com.example.springdemo.dao.User;

public interface LoginService {

    boolean signUp(String email, String userName, String password);

    Result<LoginResult> loginByUserName(String userName, String password);

    Result<LoginResult> loginByEmail(String email, String password);

    Result<LoginResult> logout(String userName, String email);
}
