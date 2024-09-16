package com.example.springdemo.service.helper;

import com.example.springdemo.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class WebSocketManager {

    private final static CopyOnWriteArraySet<WebSocketService> webSocketServerSet = new CopyOnWriteArraySet<>();

    private final static ConcurrentHashMap<Long, WebSocketService> webSocketServerMap = new ConcurrentHashMap<>();

    public static int connectionNumber() {
        return webSocketServerSet.size();
    }

    public static CopyOnWriteArraySet<WebSocketService> getWebSocketServers() {
        return webSocketServerSet;
    }

    public static WebSocketService getWebSocketServerByUserId(Long userId) {
        return webSocketServerMap.get(userId);
    }

    public static void addWebSocketServer(WebSocketService webSocketServer) {
        if (webSocketServer != null) {
            webSocketServerSet.add(webSocketServer);
            webSocketServerMap.put(webSocketServer.getUserId(), webSocketServer);
        }
    }

    public static void removeWebSocketServer(WebSocketService webSocketServer) {
        webSocketServerSet.remove(webSocketServer);
        webSocketServerMap.remove(webSocketServer.getUserId());
    }

    public static void sentToUser(Long userId, String message) {
        Session session = webSocketServerMap.get(userId).getSession();
        sentToUser(session, message);
    }

    public static void sentToUser(Session session, String message) {
        if (session == null) {
            log.error("不存在该Session，无法发送消息");
            return;
        }
        try {
            session.getAsyncRemote().sendText(message);
        } catch (Exception e) {
            log.error("errors in send message: {}", e.getMessage());
        }
    }

    public static void sentToAllUser(String message) {
        for (WebSocketService webSocketServer : webSocketServerSet) {
            sentToUser(webSocketServer.getSession(), message);
        }
        log.info("向所有用户发送WebSocket消息完毕，消息：{}", message);
    }
}

