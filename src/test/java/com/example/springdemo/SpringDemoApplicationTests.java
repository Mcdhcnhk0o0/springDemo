package com.example.springdemo;

import com.example.springdemo.config.BaiduOcrConfig;
import com.example.springdemo.bean.dao.User;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.utils.JWTUtil;
import com.example.springdemo.utils.EncryptorUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
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
    static void encryptorTest() {
        List<String> dataList = new ArrayList<>();
        dataList.add("test");
        dataList.add("pwd");
        String pwd = "wqqduan";
        for (String data: dataList) {
            String afterEncryptor = EncryptorUtil.encrypt(data, pwd);
            System.out.println(EncryptorUtil.decrypt("FjP9PE9/vnJmN8xi+aKN5k2A0PSXLb/4", pwd));
            System.out.println(afterEncryptor);
        }
    }

    @Test
    void jwtTest() {
        User user = new User();
        user.setUserId(123L);
        user.setPassword("123456");
        try {
            String token = JWTUtil.Instance.createToken(user.getUserId().toString(), user.getPassword());
            JWTUtil.Instance.verifyUserByToken(user, token);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("JWT test over");
        
    }

    public static void main(String[] args) {
        // run your individual code
        encryptorTest();
    }

}
