package com.example.springdemo;

import com.example.springdemo.dao.User;
import com.example.springdemo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SpringDemoApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testMapper() {
        System.out.println("test of mybatis plus");
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
