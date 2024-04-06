package com.example.springdemo.service.third;

import com.alibaba.fastjson.JSON;
import com.example.springdemo.bean.dto.LLMDTO;
import com.example.springdemo.config.SparkLLMConfig;
import com.example.springdemo.service.WebSocketService;
import com.example.springdemo.utils.LLMUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
// todo: 内部实现均为非线程安全，待后续改造
public class SparkLLMService extends WebSocketListener {

    private static final int WS_CLOSE_CODE = 1000;

    private static final String hostUrl = "https://spark-api.xf-yun.com/v2.1/chat";

    @Resource
    private SparkLLMConfig llmConfig;

    private Long userId;

    private String query;

    private WebSocket cachedWebSocket;

    public static StringBuilder completeAnswer = new StringBuilder();

    public static List<LLMDTO.RoleContent> historyList = new ArrayList<>();

    public WebSocket startConnection(Long userId) {
        this.userId = userId;
        this.query = "你好，以后每个回答请以颜文字结尾(*´▽｀)ノノ";
        this.cachedWebSocket = getNewWebsocket();
        return cachedWebSocket;
    }

    public WebSocket sendMessage(String message) {
        cachedWebSocket.send(message);
        return cachedWebSocket;
    }

    public WebSocket sendNewMessage(String message, Long userId) {
        this.query = message;
        this.userId = userId;
        return getNewWebsocket();
    }

    private WebSocket getNewWebsocket() {
        WebSocket webSocket = null;
        try {
            String authUrl = LLMUtil.getAuthUrl(hostUrl, llmConfig.getApiKey(), llmConfig.getApiSecret());
            log.debug("apiKey" + llmConfig.getApiKey() + ", secret: " + llmConfig.getApiSecret());
            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url(authUrl).build();
            LLMChatRecorder.getInstance().setResponseComplete(false);
            webSocket = client.newWebSocket(request, this);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return webSocket;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        initializeConnection(webSocket);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        WebSocketService.sendMessageToUser(userId, text);
        LLMDTO response = JSON.parseObject(text, LLMDTO.class);
        if (!LLMUtil.llmResponseValid(response)) {
            log.error("发生错误，错误码为：" + response.header.code);
            log.error("本次请求的sid为：" + response.header.sid);
            webSocket.close(WS_CLOSE_CODE, "connection closed due to errors in server");
        }
        List<LLMDTO.Text> textList = response.payload.choices.text;
        for (LLMDTO.Text temp : textList) {
            System.out.print(temp.content);
            completeAnswer.append(temp.content);
        }

        if (response.header.status == 2) {
            // 可以关闭连接，释放资源
            String answer = completeAnswer.toString();
            System.out.println("-> complete: " + completeAnswer);
            if (LLMUtil.canAddHistory(historyList)) {
                LLMDTO.RoleContent roleContent = new LLMDTO.RoleContent();
                roleContent.setRole("assistant");
                roleContent.setContent(answer);
                historyList.add(roleContent);
            } else {
                historyList.remove(0);
                LLMDTO.RoleContent roleContent=new LLMDTO.RoleContent();
                roleContent.setRole("assistant");
                roleContent.setContent(answer);
                historyList.add(roleContent);
            }
            completeAnswer = new StringBuilder();
            LLMChatRecorder.getInstance().setResponseComplete(true);
            LLMChatRecorder.getInstance().record(userId, historyList.get(historyList.size() - 1));
        }
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        log.error(t.getMessage());
        if (response != null) {
            log.error(response.toString());
        }
    }

    private void initializeConnection(WebSocket webSocket) {
        new ChatRequestThread(webSocket, query).start();
    }

    class ChatRequestThread extends Thread {

        private final boolean autoClose = false;
        private final String query;
        private final WebSocket webSocket;

        public ChatRequestThread(WebSocket webSocket, String query) {
            this.query = query;
            this.webSocket = webSocket;
        }

        @Override
        public void run() {
            String request = getChatRequest(query);
            if (webSocket != null) {
                LLMChatRecorder.getInstance().setResponseComplete(false);
                webSocket.send(request);
                if (autoClose) {
                    listenResponseAndClose();
                }
            }
        }

        private void listenResponseAndClose() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (LLMChatRecorder.getInstance().isResponseComplete()) {
                        webSocket.close(WS_CLOSE_CODE, "");
                    }
                }
            }, 50L, 5000L);
        }
    }

    public String getChatRequest(String query) {
        return LLMUtil.getAuthRequest(llmConfig.getAppId(), query, historyList);
    }

}
