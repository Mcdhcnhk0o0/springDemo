package com.example.springdemo.bean;

import java.util.Objects;

public enum NewsType {

    TOP("top"),
    COUNTRY("guonei"),
    INTERNATION("guoji"),
    TECHNOLOGY("keji"),
    GAME("youxi");

    private final String key;

    public String getKey() {
        return key;
    }

    NewsType(String key) { this.key = key; }

    public static NewsType fromString(String str) {
        for (NewsType type: NewsType.values()) {
            if (Objects.equals(type.key, str)) {
                return type;
            }
        }
        return null;
    }

}
