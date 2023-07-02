package com.example.springdemo.service.impl;

import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.LoginStatus;
import com.example.springdemo.bean.Result;
import com.example.springdemo.dao.User;
import com.example.springdemo.factory.LoginResultBuilder;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.service.LoginService;
import com.example.springdemo.service.UserService;
import com.example.springdemo.service.status.LoginStatusManager;
import com.example.springdemo.utils.JWTUtil;
import com.example.springdemo.utils.LoginResultInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;


@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Override
    public boolean signUp(String email, String userName, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUserName(userName);
        user.setUserNickname(userName);
        user.setPassword(password);
        userMapper.insert(user);
        return true;
    }

    @Override
    public Result<LoginResult> loginByUserName(String userName, String password) {
        User currentUser = userService.queryByUserName(userName);
        if (currentUser == null) {
            return new LoginResultBuilder()
                    .setStatusCode(LoginResultInfo.USER_NOT_EXIST.getCode())
                    .setStatusMessage(LoginResultInfo.USER_NOT_EXIST.getMessage())
                    .fail();
        }
        if (!Objects.equals(password, currentUser.getPassword())) {
            return new LoginResultBuilder()
                    .setStatusCode(LoginResultInfo.WRONG_PASSWORD.getCode())
                    .setStatusMessage(LoginResultInfo.WRONG_PASSWORD.getMessage())
                    .fail();
        }
        LoginStatus loginStatus = LoginStatusManager.getInstance().getLoginStatus(currentUser);
        LoginResultInfo loginResultInfo = LoginResultInfo.USER_ALREADY_LOGIN;
        if (loginStatus == null) {
            loginResultInfo = LoginResultInfo.LOGIN_SUCCESS;
            loginStatus = LoginStatusManager.getInstance().addLoginStatus(currentUser);
        }
        return new LoginResultBuilder()
                .setUser(privacyFilter(currentUser))
                .setToken(generateToken(currentUser))
                .setStatusCode(loginResultInfo.getCode())
                .setStatusMessage(loginResultInfo.getMessage())
                .setLatestLoginTime(loginStatus.getLoginGmt())
                .success();
    }

    @Override
    public Result<LoginResult> loginByEmail(String email, String password) {
        return null;
    }

    @Override
    public Result<LoginResult> logout(String userName, String email) {
        User currentUser = userService.queryByUserName(userName);
        if (currentUser == null) {
            return new LoginResultBuilder()
                    .setStatusCode(LoginResultInfo.USER_NOT_EXIST.getCode())
                    .setStatusMessage(LoginResultInfo.USER_NOT_EXIST.getMessage())
                    .fail();
        }
        LoginStatus loginStatus = LoginStatusManager.getInstance().getLoginStatus(currentUser);
        if (loginStatus == null) {
            return new LoginResultBuilder()
                    .setStatusCode(LoginResultInfo.USER_NOT_LOGIN.getCode())
                    .setStatusMessage(LoginResultInfo.USER_NOT_LOGIN.getMessage())
                    .fail();
        }
        loginStatus = LoginStatusManager.getInstance().removeLoginStatus(currentUser);
        return new LoginResultBuilder()
                .setUser(privacyFilter(currentUser))
                .setToken(generateToken(currentUser))
                .setStatusCode(LoginResultInfo.LOGOUT_SUCCESS.getCode())
                .setStatusMessage(LoginResultInfo.LOGOUT_SUCCESS.getMessage())
                .setLatestLoginTime(loginStatus.getLoginGmt())
                .success();
    }

    private String generateToken(User user) {
        if (user != null && user.getUserId() != null && user.getPassword() != null) {
            return JWTUtil.Instance.createToken(user.getUserId().toString(), user.getPassword());
        }
        return "";
    }

    private User privacyFilter(User user) {
        if (user != null) {
            user.setEmail("protected****" + user.getEmail());
            user.setPassword("unknown");
        }
        return user;
    }
}
