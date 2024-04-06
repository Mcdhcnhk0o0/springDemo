package com.example.springdemo.llm.provider.ali;

import com.example.springdemo.llm.message.Message;
import com.example.springdemo.llm.message.TypedMessageFactory;
import com.example.springdemo.llm.protocol.Role;
import com.example.springdemo.llm.protocol.Type;


public class AliMessageFactory implements TypedMessageFactory {

    @Override
    public com.alibaba.dashscope.common.Message createTypedMessage(Type type, Message message) {
        return com.alibaba.dashscope.common.Message.builder()
                .role(roleAdapter(message.getRole()))
                .content(message.getContent())
                .build();
    }

    @Override
    public String roleAdapter(Role role) {
        switch (role) {
            case USER: return com.alibaba.dashscope.common.Role.USER.getValue();
            case ASSISTANT: return com.alibaba.dashscope.common.Role.ASSISTANT.getValue();
        }
        return null;
    }
}
