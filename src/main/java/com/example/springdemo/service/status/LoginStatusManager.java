package com.example.springdemo.service.status;

import com.example.springdemo.bean.LoginStatus;
import com.example.springdemo.dao.User;

import java.util.HashMap;
import java.util.Map;

public class LoginStatusManager {

    private final Map<Long, LoginStatus> loginStatusMap = new HashMap<>();

    public static LoginStatusManager getInstance() {
        return InnerClass.instance;
    }

    public boolean getLoginStatus(User user) {
        return loginStatusMap.containsKey(user.getUserId());
    }

    public void addLoginStatus(User user) {
        LoginStatus status = new LoginStatus();
        status.setLogin(true);
        status.setLoginGmt(String.valueOf(System.currentTimeMillis()));
        loginStatusMap.put(user.getUserId(), status);
    }

    public void removeLoginStatus(User user) {
        loginStatusMap.remove(user.getUserId());
    }

    private static class InnerClass {
        static final LoginStatusManager instance = new LoginStatusManager();
    }
}
