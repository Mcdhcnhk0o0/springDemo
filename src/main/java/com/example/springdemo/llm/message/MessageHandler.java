package com.example.springdemo.llm.message;

public interface MessageHandler {

    boolean beforeMessage(Message message);

    String processMessage(Message message);

    boolean afterMessage(Message message);

}
