package com.example.springdemo.service.impl;

import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.Result;
import com.example.springdemo.dao.User;
import com.example.springdemo.factory.LoginResultFactory;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.service.LoginService;
import com.example.springdemo.service.status.LoginStatusManager;
import com.example.springdemo.utils.JWTUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean signUp(User user) {
        userMapper.insert(user);
        return true;
    }

    @Override
    public Result<LoginResult> login(User user) {
        if (LoginStatusManager.getInstance().getLoginStatus(user)) {
            return LoginResultFactory.loginFailed(401001, "用户已登录，请勿重复登录");
        }
        LoginStatusManager.getInstance().addLoginStatus(user);
        Result<LoginResult> result = LoginResultFactory.loginSuccess(201001, "登录成功");
        result.getData().setUser(user);
        if (user.getUserId() != null && user.getPassword() != null) {
            result.getData().setToken(JWTUtil.Instance.createToken(user.getUserId().toString(), user.getPassword()));
        }
        return result;
    }

    @Override
    public Result<LoginResult> logout(User user) {
        if (!LoginStatusManager.getInstance().getLoginStatus(user)) {
            return LoginResultFactory.loginFailed(401001, "用户未登录");
        }
        LoginStatusManager.getInstance().removeLoginStatus(user);
        return LoginResultFactory.loginSuccess(201002, "注销成功");
    }
}
