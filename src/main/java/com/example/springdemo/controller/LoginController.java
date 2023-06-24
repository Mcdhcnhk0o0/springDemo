package com.example.springdemo.controller;

import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.Result;
import com.example.springdemo.dao.User;
import com.example.springdemo.factory.LoginResultFactory;
import com.example.springdemo.service.LoginService;
import com.example.springdemo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Resource
    private LoginService loginService;

    @Resource
    private UserService userService;

    @GetMapping("/signup")
    public boolean signIn(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password) {
        User user = new User();
        user.setEmail(email);
        user.setUserName(name);
        user.setUserNickname(name);
        user.setPassword(password);
        return loginService.signUp(user);
    }

    @PassToken
    @GetMapping("loginByUserName")
    public Result<LoginResult> loginByUserName(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password) {
        User currentUser = userService.queryByUserName(name);
        if (currentUser == null) {
            return LoginResultFactory.loginFailed(402001, "用户不存在");
        }
        if (!Objects.equals(password, currentUser.getPassword())) {
            return LoginResultFactory.loginFailed(402002, "用户名与密码不匹配");
        }
        return loginService.login(currentUser);
    }

    @UserLoginToken
    @GetMapping("logout")
    public Result<LoginResult> logout(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password) {
        User currentUser = userService.queryByUserName(name);
        if (currentUser == null) {
            return LoginResultFactory.loginFailed(402001, "用户不存在");
        }
        return loginService.logout(currentUser);
    }

}
