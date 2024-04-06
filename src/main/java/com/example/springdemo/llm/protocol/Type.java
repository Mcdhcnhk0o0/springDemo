package com.example.springdemo.llm.protocol;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public enum Type {

    TONG_YI,
    WEN_XIN,
    HUN_YUAN,
    ZHI_PU,
    YUN_QUE,
    OTHERS;

    private static final Map<Type, Long> codes = new HashMap<Type, Long>() {{
        put(Type.TONG_YI, 9960001L);
        put(Type.WEN_XIN, 9960002L);
        put(Type.HUN_YUAN, 9960003L);
        put(Type.ZHI_PU, 9960004L);
        put(Type.YUN_QUE, 9960005L);
        put(Type.OTHERS, 9970000L);
    }};

    public Long toCode() {
        return codes.get(this);
    }

    @Nullable
    public static Type fromCode(Long code) {
        for (Map.Entry<Type, Long> entry: codes.entrySet()) {
            if (Objects.equals(entry.getValue(), code)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
