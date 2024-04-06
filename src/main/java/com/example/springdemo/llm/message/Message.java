package com.example.springdemo.llm.message;

import com.example.springdemo.llm.protocol.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Role role;
    private String content;

}
