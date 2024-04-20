package com.example.springdemo.service;


import com.alibaba.fastjson.JSON;
import com.example.springdemo.bean.dto.message.WebsocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketService {

    private Session session;

    private static final ConcurrentHashMap<Long, Session> sessionPool = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Session, Long> userPool = new ConcurrentHashMap<>();

    private static final CopyOnWriteArraySet<WebSocketService> webSocketServices = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Long userId) {
        log.info("connection establishing... userId: " + userId);
        try {
            Session historySession = sessionPool.get(userId);
            if (historySession != null) {
                webSocketServices.remove(historySession);
                historySession.close();
            }
        } catch (Exception e) {
            log.info("errors in close current session: " + e.getMessage());
        }
        this.session = session;
        webSocketServices.add(this);
        userPool.put(session, userId);
        sessionPool.put(userId, session);
        log.info("connection established! Total number of service: " + webSocketServices.size());
    }

    @OnError
    public void onError(Throwable e) {
        log.error(e.getMessage());
    }

    @OnClose
    public void onClose() {
        webSocketServices.remove(this);
        log.info("websocket disconnected! Total number of service: " + webSocketServices.size());
    }

    public boolean connectedWith(Long userId) {
        return sessionPool.containsKey(userId);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("message received! Content: " + message);
        Long userId = userPool.get(session);
        sendMessageToUser(userId, "I received: " + message);
    }

    public void sendToUser(Long userId, String message) {
        log.info("send message to " + userId + ", message: " + message);
        Session session = sessionPool.get(userId);
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("errors in sending message: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void sendToUser(Long userId, WebsocketMessage message) {
        String messageStr = JSON.toJSONString(message);
        sendToUser(userId, messageStr);
    }

    public static void sendMessageToUser(Long userId, String message) {
        log.info("send message to " + userId + ", message: " + message);
        Session session = sessionPool.get(userId);
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("errors in sending message: " + Arrays.toString(e.getStackTrace()));
        }
    }

}
