package com.example.springdemo.service;


import com.alibaba.fastjson.JSON;
import com.example.springdemo.bean.dto.message.WebsocketMessage;
import com.example.springdemo.service.helper.WebSocketManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


@Slf4j
@Data
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketService {

    private Long userId;

    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Long userId) {
        this.userId = userId;
        this.session = session;
        WebSocketManager.addWebSocketServer(this);
        log.info("connection establishing... userId: " + userId);
        log.info("connection established! Total number of service: " + WebSocketManager.connectionNumber());
    }

    @OnError
    public void onError(Throwable e) {
        log.error("error occurred: " + e.toString());
    }

    @OnClose
    public void onClose() {
        WebSocketManager.removeWebSocketServer(this);
        log.info("websocket disconnected! Total number of service: " + WebSocketManager.connectionNumber());
    }

    public boolean connectedWith(Long userId) {
        return WebSocketManager.getWebSocketServerByUserId(userId) != null;
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("message received! Content: " + message);
        sendMessageToUser(userId, "I received: " + message);
    }

    public void sendToUser(Long userId, String message) {
        log.info("send message to " + userId + ", message: " + message);
        WebSocketManager.sentToUser(userId, message);
    }

    public void broadcastToAll(String message) {
        WebSocketManager.sentToAllUser(message);
    }

    public void sendToUser(Long userId, WebsocketMessage message) {
        String messageStr = JSON.toJSONString(message);
        sendToUser(userId, messageStr);
    }

    public static void sendMessageToUser(Long userId, String message) {
        log.info("send message to " + userId + ", message: " + message);
        WebSocketManager.sentToUser(userId, message);
    }

}

