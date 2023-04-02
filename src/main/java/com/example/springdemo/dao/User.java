package com.example.springdemo.dao;

import lombok.Data;

@Data
public class User {

    private Integer userId;
    private String userName;
    private String userNickname;
    private String password;
    private String extraInfo;

}
