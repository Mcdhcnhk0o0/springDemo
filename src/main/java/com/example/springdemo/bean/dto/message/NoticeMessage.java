package com.example.springdemo.bean.dto.message;

import java.util.UUID;

public class NoticeMessage extends WebsocketMessage {

    public static class Builder {

        private final NoticeMessage message = new NoticeMessage();

        public Builder notice(String notice) {
            message.setId(UUID.randomUUID().toString());
            message.setType("notice");
            message.setContent(notice);
            return this;
        }

        public Builder tag(String tag) {
            message.setTag(tag);
            return this;
        }

        public NoticeMessage build() {
            return message;
        }

    }

}
