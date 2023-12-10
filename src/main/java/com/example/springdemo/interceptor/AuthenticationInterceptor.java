package com.example.springdemo.interceptor;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.dao.User;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.utils.JWTUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (!userLoginToken.required()) {
                return false;
            }
            String token = request.getHeader("token");
            if (token == null) {
                throw new RuntimeException("无效token，请重新登录");
            }
            String userId = "";
            try {
                userId = JWTUtil.Instance.getUserIdFromToken(token);
            } catch (Exception e) {
                throw new RuntimeException("token校验失败, " + e.getMessage());
            }
            if (userId.isEmpty()) {
                throw new RuntimeException("token校验失败, 请重新登录");
            }
            Object redisStatus = redisTemplate.opsForValue().get(userId);
            if (redisStatus == null) {
                throw new RuntimeException("用户登录已失效，请重新登录");
            }
            User currentUser = null;
            try {
                currentUser = JSON.parseObject((String) redisStatus, User.class);
            } catch (Exception e) {
                throw new RuntimeException("用户验证失败，请重新登录");
            }
            try {
                JWTUtil.Instance.verifyUserByToken(currentUser, token);
            } catch (Exception e) {
                throw new RuntimeException("token验证失败, " + e.getMessage());
            }
            return true;
        }
        return false;
    }

}
