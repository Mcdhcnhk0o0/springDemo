package com.example.springdemo.llm.message;

public interface MessageAsyncListener {

    void onStream(String messageStream);

    void onFinish();

}
