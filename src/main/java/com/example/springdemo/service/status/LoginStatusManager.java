package com.example.springdemo.service.status;

import com.example.springdemo.bean.LoginStatus;
import com.example.springdemo.dao.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginStatusManager {

    private final Map<Long, LoginStatus> loginStatusMap = new HashMap<>();

    public static LoginStatusManager getInstance() {
        return InnerClass.instance;
    }

    public LoginStatus getLoginStatus(User user) {
        return loginStatusMap.get(user.getUserId());
    }

    public LoginStatus addLoginStatus(User user) {
        LoginStatus status = new LoginStatus();
        status.setLogin(true);
        status.setLoginGmt(String.valueOf(new Date(System.currentTimeMillis())));
        loginStatusMap.put(user.getUserId(), status);
        return status;
    }

    public LoginStatus removeLoginStatus(User user) {
        return loginStatusMap.remove(user.getUserId());
    }

    private static class InnerClass {
        static final LoginStatusManager instance = new LoginStatusManager();
    }
}
