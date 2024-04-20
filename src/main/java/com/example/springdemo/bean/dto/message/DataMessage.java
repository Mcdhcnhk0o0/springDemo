package com.example.springdemo.bean.dto.message;

import java.util.UUID;

public class DataMessage extends WebsocketMessage {

    public static class Builder {

        private final DataMessage message = new DataMessage();

        public <T> Builder data(T data) {
            message.setId(UUID.randomUUID().toString());
            message.setType("data");
            message.setContent(data);
            return this;
        }

        public Builder tag(String tag) {
            message.setTag(tag);
            return this;
        }

        public DataMessage build() {
            return message;
        }

    }

}
