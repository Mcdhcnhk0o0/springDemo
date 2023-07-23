package com.example.springdemo.service.impl;

import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.Result;
import com.example.springdemo.dao.User;
import com.example.springdemo.factory.LoginResultBuilder;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.service.LoginService;
import com.example.springdemo.service.UserService;
import com.example.springdemo.utils.JWTUtil;
import com.example.springdemo.utils.LoginResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

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
        if (currentUser == null || currentUser.getUserId() == null) {
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
        String userIdStr = currentUser.getUserId().toString();
        Object latestLoginInfo = getLatestLoginInfoFromRedis(userIdStr);
        LoginResultInfo loginResultInfo = LoginResultInfo.USER_ALREADY_LOGIN;
        if (latestLoginInfo == null) {
            loginResultInfo = LoginResultInfo.LOGIN_SUCCESS;
            log.debug("new login user: {}", currentUser.getUserId().toString());
        }
        setLatestLoginTimeToRedis(userIdStr);
        return new LoginResultBuilder()
                .setToken(generateToken(currentUser))
                .setUser(privacyFilter(currentUser))
                .setStatusCode(loginResultInfo.getCode())
                .setStatusMessage(loginResultInfo.getMessage())
                .setLatestLoginTime((String) latestLoginInfo)
                .success();
    }

    @Override
    public Result<LoginResult> loginByEmail(String email, String password) {
        return null;
    }

    @Override
    public Result<LoginResult> logout(String userName, String email) {
        User currentUser = userService.queryByUserName(userName);
        if (currentUser == null || currentUser.getUserId() == null) {
            return new LoginResultBuilder()
                    .setStatusCode(LoginResultInfo.USER_NOT_EXIST.getCode())
                    .setStatusMessage(LoginResultInfo.USER_NOT_EXIST.getMessage())
                    .fail();
        }
        String userIdStr = currentUser.getUserId().toString();
        Object latestLoginInfo = getLatestLoginInfoFromRedis(userIdStr);
        if (latestLoginInfo == null) {
            return new LoginResultBuilder()
                    .setStatusCode(LoginResultInfo.USER_NOT_LOGIN.getCode())
                    .setStatusMessage(LoginResultInfo.USER_NOT_LOGIN.getMessage())
                    .fail();
        }
        removeLatestLoginTimeInRedis(userIdStr);
        return new LoginResultBuilder()
                .setUser(privacyFilter(currentUser))
                .setToken(generateToken(currentUser))
                .setStatusCode(LoginResultInfo.LOGOUT_SUCCESS.getCode())
                .setStatusMessage(LoginResultInfo.LOGOUT_SUCCESS.getMessage())
                .setLatestLoginTime((String) latestLoginInfo)
                .success();
    }

    private Object getLatestLoginInfoFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private void removeLatestLoginTimeInRedis(String key) {
        redisTemplate.delete(key);
    }

    private void setLatestLoginTimeToRedis(String key) {
        redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()));
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    private String generateToken(User user) {
        if (user != null && user.getUserId() != null && user.getPassword() != null) {
            return JWTUtil.Instance.createToken(user.getUserId().toString(), user.getPassword());
        }
        return "";
    }

    private User privacyFilter(User user) {
        // bugfix：改動現有的user對象會引起不可預期的問題
        User outUser = new User();
        if (user != null) {
            outUser.setUserId(user.getUserId());
            outUser.setUserName(user.getUserName());
            outUser.setUserNickname(user.getUserNickname());
            outUser.setPassword("unknown");
            outUser.setEmail("protected****" + user.getEmail());
            outUser.setExtraInfo(user.getExtraInfo());
        }
        return outUser;
    }
}
