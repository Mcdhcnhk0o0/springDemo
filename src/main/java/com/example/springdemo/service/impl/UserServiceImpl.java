package com.example.springdemo.service.impl;

import com.example.springdemo.dao.User;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean addUser(User user) {
        userMapper.insert(user);
        return true;
    }

    @Override
    public boolean removeUser(User user) {
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }


    @Override
    public User queryById(int id) {
        return null;
    }

    @Override
    public User queryByName(String name) {
        return null;
    }

    @Override
    public boolean modifyNicknameById(int id) {
        return false;
    }
}
