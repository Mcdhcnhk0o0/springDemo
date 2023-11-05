package com.example.springdemo.bean.response;

import java.util.List;

public class LLMResponse {

    public Header header;
    public Payload payload;

    public static class Header {
        public int code;
        public int status;
        public String sid;
    }

    public static class Payload {
        public Choices choices;
    }

    public static class Choices {
        public List<Text> text;
    }

    public static class Text {
        public String role;
        public String content;
    }

    public static class RoleContent {

        String role;
        String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
