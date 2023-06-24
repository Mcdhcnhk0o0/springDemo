package com.example.springdemo.bean;

import com.example.springdemo.dao.User;
import lombok.Data;

@Data
public class LoginResult {

    Boolean success;
    Integer statusCode;
    String statusMessage;
    String token;
    User user;

}
