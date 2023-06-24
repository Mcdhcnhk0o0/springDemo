package com.example.springdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public User queryByUserId(Long id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        List<User> userList = userMapper.selectList(wrapper);
        return userList == null ? null : userList.get(0);
    }

    @Override
    public User queryByUserName(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", name);
        List<User> userList = userMapper.selectList(wrapper);
        return userList == null ? null : userList.get(0);
    }

    @Override
    public boolean modifyNicknameByUserId(Long id) {
        return false;
    }
}
