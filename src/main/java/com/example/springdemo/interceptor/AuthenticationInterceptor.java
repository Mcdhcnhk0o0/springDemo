package com.example.springdemo.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.dao.User;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.utils.JWTUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

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
                throw new RuntimeException("token校验失败");
            }
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            List<User> userList = userMapper.selectList(wrapper);
            if (userList == null || userList.size() <= 0) {
                throw new RuntimeException("用户不存在，请重新登录");
            }
            User targetUser = userList.get(0);
            try {
                JWTUtil.Instance.verifyUserByToken(targetUser, token);
            } catch (Exception e) {
                throw new RuntimeException("token验证失败");
            }
            return true;
        }
        return false;
    }

}
