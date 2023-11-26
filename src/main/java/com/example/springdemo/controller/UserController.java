package com.example.springdemo.controller;

import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.bean.dao.User;
import com.example.springdemo.bean.dao.UserDetail;
import com.example.springdemo.service.UserDetailService;
import com.example.springdemo.service.UserService;
import com.example.springdemo.bean.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserDetailService userDetailService;

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User addUser(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password) {
        return userService.addUser(email, name, password);
    }

    @UserLoginToken
    @GetMapping(value = "/get")
    public Result<UserVO> getUserInfo(
            @RequestParam(value = "userId") Long userId
    ) {
        User user = userService.getUserById(userId);
        UserDetail userDetail = userDetailService.getUserDetail(userId);
        return new Result<UserVO>().success(new UserVO(user, userDetail));
    }

    @UserLoginToken
    @GetMapping(value = "/get/detail")
    public Result<UserDetail> getUserDetail(
            @RequestParam(value = "userId") Long userId
    ) {
        UserDetail userDetail = userDetailService.getUserDetail(userId);
        return new Result<UserDetail>().success(userDetail);
    }

    @UserLoginToken
    @GetMapping(value = "/modify/detail")
    public Result<Boolean> modifyUserDetail(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "birthday", required = false) String birthday,
            @RequestParam(value = "avatar", required = false) String avatar,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "introduction", required = false) String introduction
    ) {
        userDetailService.modifyUserDetail(userId, gender, birthday, avatar, address, introduction);
        return new Result<Boolean>().success(true);
    }

}
