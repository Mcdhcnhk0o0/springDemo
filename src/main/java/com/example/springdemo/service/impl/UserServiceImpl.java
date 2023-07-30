package com.example.springdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.springdemo.dao.User;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.service.UserService;
import com.example.springdemo.utils.UserIdUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User addUser(String email, String userName, String password) {
        User user = new User();
        user.setUserId(UserIdUtil.INSTANCE.generateUserId(email));
        user.setEmail(email);
        user.setUserName(userName);
        user.setUserNickname(userName);
        user.setPassword(password);
        userMapper.insert(user);
        return user;
    }

    @Override
    public boolean modifyUserName(Long id, String userName, String nickName) {
        User user = getUserById(id);
        if (user == null) {
            return false;
        }
        if (userName != null) {
            user.setUserName(userName);
        }
        if (nickName != null) {
            user.setUserNickname(nickName);
        }
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", id);
        userMapper.update(user, wrapper);
        return true;
    }

    @Override
    public boolean modifyPassword(Long id, String password) {
        User user = getUserById(id);
        if (user == null || password == null) {
            return false;
        }
        user.setPassword(password);
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", id);
        userMapper.update(user, wrapper);
        return true;
    }

    @Override
    public boolean removeUser(Long id) {
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }


    @Override
    public User getUserById(Long id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        List<User> userList = userMapper.selectList(wrapper);
        return userList == null ? null : userList.get(0);
    }

    @Override
    public User getUserByEmail(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        List<User> userList = userMapper.selectList(wrapper);
        return userList == null ? null : userList.get(0);
    }

}
