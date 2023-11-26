package com.example.springdemo.bean.vo;

import com.example.springdemo.bean.dao.User;
import lombok.Data;

@Data
public class LoginVO {

    Boolean success;
    Integer statusCode;
    String statusMessage;
    String token;
    String latestLoginTime;
    User user;

}
