package com.example.springdemo.controller;

import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.Result;
import com.example.springdemo.service.LoginService;
import com.example.springdemo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Resource
    private LoginService loginService;

    @GetMapping("/signup")
    public boolean signUp(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password) {

        return loginService.signUp(email, name, password);

    }

    @PassToken
    @GetMapping("loginByUserName")
    public Result<LoginResult> loginByUserName(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password) {

        return loginService.loginByUserName(userName, password);

    }

    @UserLoginToken
    @GetMapping("logout")
    public Result<LoginResult> logout(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "email", required = false) String email) {

        return loginService.logout(name, email);

    }

}
