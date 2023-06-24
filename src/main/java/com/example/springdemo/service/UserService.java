package com.example.springdemo.service;

import com.example.springdemo.dao.User;

import java.util.List;

public interface UserService {

    boolean addUser(User user);

    boolean removeUser(User user);

    List<User> getAllUsers();

    User queryByUserId(Long id);

    User queryByUserName(String name);

    boolean modifyNicknameByUserId(Long id);

}
