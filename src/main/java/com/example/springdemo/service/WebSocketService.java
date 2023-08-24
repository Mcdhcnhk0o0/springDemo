package com.example.springdemo.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketService {

    private Session session;

    private static final ConcurrentHashMap<Long, Session> sessionPool = new ConcurrentHashMap<>();

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

    @OnMessage
    public void onMessage(String message) {
        log.info("message received! Content: " + message);
        sendMessageToUser(123L, "I received: " + message);
    }

    public static void sendMessageToUser(Long userId, String message) {
        log.info("send message to " + userId + ", message: " + message);
        Session session = sessionPool.get(userId);
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("errors in sending message: " + e.getMessage());
        }
    }

}
