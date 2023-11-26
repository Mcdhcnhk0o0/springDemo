package com.example.springdemo.bean.vo;

import com.example.springdemo.bean.dao.User;
import lombok.Data;

@Data
public class SignVO {

    Boolean success;
    Integer statusCode;
    String statusMessage;
    User user;

}
