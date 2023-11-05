package com.example.springdemo.bean.result;

import com.example.springdemo.dao.User;
import lombok.Data;

import java.util.List;

@Data
public class LLMResult {

    User user;
    String token;
    List<String> displayText;

}
