package com.example.springdemo;

import com.example.springdemo.config.BaiduOcrConfig;
import com.example.springdemo.dao.User;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.utils.JWTUtil;
import com.example.springdemo.utils.JasyptEncryptorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SpringDemoApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BaiduOcrConfig customConfig;

    @Test
    void contextLoads() {
    }

    @Test
    void testMapper() {
        System.out.println("test of mybatis plus");
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    void configurationTest() {
        System.out.println(customConfig.getAppId());
        System.out.println(customConfig.getApiKey());
        System.out.println(customConfig.getApiSecret());
    }

    @Test
    void encryptorTest() {
        String data = "321312312312";
        String pwd = "hello";
        String afterEncryptor = JasyptEncryptorUtil.encrypt(data, pwd);
        System.out.println(afterEncryptor);
        String before = JasyptEncryptorUtil.decrypt(afterEncryptor, pwd);
        System.out.println(before);
    }

    @Test
    void jwtTest() {
        User user = new User();
        user.setUserId(123L);
        user.setPassword("123456");
        String token = JWTUtil.Instance.createToken(user.getUserId().toString(), user.getPassword());
        try {
            JWTUtil.Instance.verifyUserByToken(user, token);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("JWT test over");



    }

}
