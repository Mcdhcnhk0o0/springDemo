package com.example.springdemo.bean.result;

import com.example.springdemo.dao.User;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class LoginResult {

    Boolean success;
    Integer statusCode;
    String statusMessage;
    String token;
    String latestLoginTime;
    User user;

}
