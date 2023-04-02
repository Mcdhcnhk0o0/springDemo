package com.example.springdemo.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springdemo.dao.User;
import org.apache.ibatis.annotations.Mapper;

public interface UserMapper extends BaseMapper<User> {

}
