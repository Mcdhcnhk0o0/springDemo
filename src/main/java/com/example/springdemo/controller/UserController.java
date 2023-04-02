package com.example.springdemo.controller;

import com.example.springdemo.dao.User;
import com.example.springdemo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping(value = "/get")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/add")
    public boolean addUser(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password){
        User user = new User();
        user.setUserName(name);
        user.setPassword(password);
        return userService.addUser(user);
    }

}
