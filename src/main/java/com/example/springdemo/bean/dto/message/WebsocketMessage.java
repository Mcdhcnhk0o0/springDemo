package com.example.springdemo.bean.dto.message;


import lombok.Data;

@Data
public class WebsocketMessage {

    private String id;
    private String type;
    private Object content;
    private String tag;

}
