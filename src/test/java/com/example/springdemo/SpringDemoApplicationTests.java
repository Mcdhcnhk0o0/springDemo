package com.example.springdemo;

import com.example.springdemo.config.BaiduOcrConfig;
import com.example.springdemo.dao.User;
import com.example.springdemo.mapper.UserMapper;
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
        String data = "GLXCMWmKAVNesI8qCPxrl5kjLBtL7sv9";
        String pwd = "wqqduan";
        String afterEncryptor = JasyptEncryptorUtil.encrypt(data, pwd);
        System.out.println(afterEncryptor);
        String before = JasyptEncryptorUtil.decrypt(afterEncryptor, pwd);
        System.out.println(before);
    }

}
