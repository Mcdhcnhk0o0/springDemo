package com.example.springdemo.service;

import com.example.springdemo.dao.User;

import java.util.List;

public interface UserService {

    boolean addUser(User user);

    boolean removeUser(User user);

    List<User> getAllUsers();

    User queryById(int id);

    User queryByName(String name);

    boolean modifyNicknameById(int id);

}
