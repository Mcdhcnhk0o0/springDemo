package com.example.springdemo.llm.provider;

import com.example.springdemo.llm.protocol.Role;
import com.example.springdemo.llm.protocol.Type;
import com.volcengine.helper.Const;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;

import java.util.HashMap;
import java.util.Map;

public class RoleAdapter {

    private static final Map<Role, Map<Type, String>> roleMapper = new HashMap<>();

    static {
        roleMapper.put(Role.USER, new HashMap<>());
        roleMapper.put(Role.ASSISTANT, new HashMap<>());
        roleMapper.put(Role.USER, new HashMap<Type, String>() {{
            put(Type.TONG_YI, com.alibaba.dashscope.common.Role.USER.getValue());
            put(Type.WEN_XIN, "user");
            put(Type.YUN_QUE, Const.MaasChatRoleOfUser);
            put(Type.ZHI_PU, ChatMessageRole.USER.value());
            put(Type.HUN_YUAN, "user");
        }});
        roleMapper.put(Role.ASSISTANT, new HashMap<Type, String>() {{
            put(Type.TONG_YI, com.alibaba.dashscope.common.Role.ASSISTANT.getValue());
            put(Type.WEN_XIN, "assistant");
            put(Type.YUN_QUE, Const.MaasChatRoleOfAssistant);
            put(Type.ZHI_PU, ChatMessageRole.ASSISTANT.value());
            put(Type.HUN_YUAN, "assistant");
        }});
    }


    public static String adapt(Role role, Type type) {
        return roleMapper.get(role).get(type);
    }

    public static synchronized void add(Role role, Type type, String key) {
        roleMapper.get(role).put(type, key);
    }

}
