package com.example.springdemo.llm.provider.bytedance;

import com.example.springdemo.llm.message.Message;
import com.example.springdemo.llm.message.TypedMessageFactory;
import com.example.springdemo.llm.protocol.Role;
import com.example.springdemo.llm.protocol.Type;
import com.volcengine.helper.Const;
import com.volcengine.model.maas.api.Api;


public class ByteDanceMessageFactory implements TypedMessageFactory {

    @Override
    public Api.Message createTypedMessage(Type type, Message message) {
        return Api.Message.newBuilder()
                .setRole(roleAdapter(message.getRole()))
                .setContent(message.getContent())
                .build();
    }

    @Override
    public String roleAdapter(Role role) {
        switch (role) {
            case USER: return Const.MaasChatRoleOfUser;
            case ASSISTANT: return Const.MaasChatRoleOfAssistant;
        }
        return null;
    }

}
