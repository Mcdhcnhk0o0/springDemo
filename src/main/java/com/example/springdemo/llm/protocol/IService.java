package com.example.springdemo.llm.protocol;

import com.example.springdemo.llm.message.Message;
import com.example.springdemo.llm.message.MessageAsyncListener;

import java.util.List;

public interface IService {

    void init();

    String invokeSync(List<Message> messages);

    String invokeAsync(List<Message> messages, MessageAsyncListener responseListener);

}
