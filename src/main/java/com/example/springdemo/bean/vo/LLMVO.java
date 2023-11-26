package com.example.springdemo.bean.vo;

import com.example.springdemo.bean.dao.User;
import lombok.Data;

import java.util.List;

@Data
public class LLMVO {

    User user;
    String token;
    List<String> displayText;

}
