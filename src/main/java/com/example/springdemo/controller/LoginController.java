package com.example.springdemo.controller;

import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.result.LoginResult;
import com.example.springdemo.bean.result.Result;
import com.example.springdemo.service.LoginService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Resource
    private LoginService loginService;

    @PassToken
    @GetMapping("/signup")
    public boolean signUp(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password) {

        return loginService.signUp(email, name, password);

    }

    @PassToken
    @GetMapping("loginByEmail")
    public Result<LoginResult> loginByEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password) {

        return loginService.loginByEmail(email, password);

    }

    @UserLoginToken
    @GetMapping("logout")
    public Result<LoginResult> logout(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "email", required = false) String email) {

        return loginService.logout(userId, email);

    }

}
// UPDATE mysql.user SET authentication_string='' WHERE user='root';  