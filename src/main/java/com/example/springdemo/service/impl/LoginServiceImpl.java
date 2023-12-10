package com.example.springdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.springdemo.bean.vo.LoginVO;
import com.example.springdemo.bean.vo.SignVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.bean.dao.User;
import com.example.springdemo.factory.LoginResultBuilder;
import com.example.springdemo.service.LoginService;
import com.example.springdemo.service.UserDetailService;
import com.example.springdemo.service.UserService;
import com.example.springdemo.utils.JWTUtil;
import com.example.springdemo.bean.vo.protocol.LoginResultInfo;
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
    private UserService userService;

    @Resource
    private UserDetailService userDetailService;

    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result<SignVO> signUp(String email, String userName, String password) {
        User user = userService.addUser(email, userName, password);
        if (user != null && user.getUserId() != null) {
            userDetailService.addUserDetail(user.getUserId(), null, null, null, null, null);
            SignVO signVO = new SignVO();
            signVO.setUser(privacyFilter(user));
            return new Result<SignVO>().success(signVO);
        }
        return new Result<SignVO>().fail();
    }

    @Override
    public Result<LoginVO> loginByEmail(String email, String password) {
        User currentUser = userService.getUserByEmail(email);
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
        setLatestLoginTimeToRedis(userIdStr, currentUser);
        return new LoginResultBuilder()
                .setToken(generateToken(currentUser))
                .setUser(privacyFilter(currentUser))
                .setStatusCode(loginResultInfo.getCode())
                .setStatusMessage(loginResultInfo.getMessage())
                .success();
    }

    @Override
    public Result<LoginVO> logout(Long userId, String email) {
        User currentUser = userService.getUserById(userId);
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
                .success();
    }

    private User getLatestLoginInfoFromRedis(String key) {
        String userStr = (String) redisTemplate.opsForValue().get(key);
        User currentUser = null;
        try {
            currentUser = JSON.parseObject(userStr, User.class);
        } catch (Exception e) {
            // redis中存储内容不一定为user，反序列化出错时忽略即可，登录校验通过后会刷新
            e.printStackTrace();
        }
        return currentUser;
    }

    private void removeLatestLoginTimeInRedis(String key) {
        redisTemplate.delete(key);
    }

    private void setLatestLoginTimeToRedis(String key, User user) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(user));
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
