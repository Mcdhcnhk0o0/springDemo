package com.example.springdemo.service;

import com.example.springdemo.bean.vo.LoginVO;
import com.example.springdemo.bean.vo.SignVO;
import com.example.springdemo.bean.vo.protocol.Result;


public interface LoginService {

    Result<SignVO> signUp(String email, String userName, String password);

    Result<LoginVO> loginByEmail(String email, String password);

    Result<LoginVO> logout(Long userId, String email);
}
