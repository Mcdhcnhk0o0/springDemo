package com.example.springdemo.service.third;

import com.alibaba.fastjson.JSON;
import com.example.springdemo.bean.response.LLMResponse;
import com.example.springdemo.config.SparkLLMConfig;
import com.example.springdemo.utils.SparkLLMUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SparkLLMService extends WebSocketListener {

    private static final int WS_CLOSE_CODE = 1000;

    private static final String hostUrl = "https://spark-api.xf-yun.com/v2.1/chat";

    @Resource
    private SparkLLMConfig llmConfig;

    public static StringBuilder completeAnswer = new StringBuilder();

    public static List<LLMResponse.RoleContent> historyList = new ArrayList<>();

    public WebSocket startConnection() {
        WebSocket webSocket = null;
        try {
            String authUrl = SparkLLMUtil.getAuthUrl(hostUrl, llmConfig.getApiKey(), llmConfig.getApiSecret());
            log.debug("apiKey" + llmConfig.getApiKey() + ", secret: " + llmConfig.getApiSecret());
            OkHttpClient client = new OkHttpClient.Builder().build();
            String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
            Request request = new Request.Builder().url(url).build();
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
        LLMResponse response = JSON.parseObject(text, LLMResponse.class);
        if (SparkLLMUtil.llmResponseValid(response)) {
            log.error("发生错误，错误码为：" + response.header.code);
            log.error("本次请求的sid为：" + response.header.sid);
            webSocket.close(WS_CLOSE_CODE, "connection closed due to errors in server");
        }
        List<LLMResponse.Text> textList = response.payload.choices.text;
        for (LLMResponse.Text temp : textList) {
            System.out.print(temp.content);
            completeAnswer.append(temp.content);
        }
        String answer = completeAnswer.toString();

        if (response.header.status == 2) {
            // 可以关闭连接，释放资源
            System.out.println();
            System.out.println("*************************************************************************************");
            if(SparkLLMUtil.canAddHistory(historyList)){
                LLMResponse.RoleContent roleContent=new LLMResponse.RoleContent();
                roleContent.setRole("assistant");
                roleContent.setContent(answer);
                historyList.add(roleContent);
            }else{
                historyList.remove(0);
                LLMResponse.RoleContent roleContent=new LLMResponse.RoleContent();
                roleContent.setRole("assistant");
                roleContent.setContent(answer);
                historyList.add(roleContent);
            }
            LLMChatRecorder.getInstance().record(0L, historyList.get(historyList.size() - 1));
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
        String request = SparkLLMUtil.getAuthRequest(llmConfig.getAppId(), "Hello~", historyList);
        if (webSocket != null) {
            webSocket.send(request);
        }
    }


}
