package com.example.springdemo.service;

import com.example.springdemo.bean.dao.User;

import java.util.List;

public interface UserService {

    User addUser(String email, String userName, String password);

    boolean modifyUserName(Long id, String userName, String nickName);

    boolean modifyPassword(Long id, String password);

    boolean removeUser(Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByEmail(String name);

}
