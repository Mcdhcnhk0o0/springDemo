package com.example.springdemo.llm.message;

import com.example.springdemo.llm.protocol.Role;
import com.example.springdemo.llm.protocol.Type;

public interface TypedMessageFactory {

    Object createTypedMessage(Type type, Message message);

    String roleAdapter(Role role);

}
