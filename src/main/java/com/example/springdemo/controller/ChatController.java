package com.example.springdemo.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.alibaba.dashscope.utils.JsonUtils;
import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.dao.ChatRecord;
import com.example.springdemo.bean.vo.LLMVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.ChatService;
import com.example.springdemo.service.LLMChatService;
import com.example.springdemo.service.WebSocketService;
import com.example.springdemo.utils.UserInfoUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.SSEResponseModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import com.tencentcloudapi.hunyuan.v20230901.models.ChatStdRequest;
import com.tencentcloudapi.hunyuan.v20230901.models.ChatStdResponse;
import com.tencentcloudapi.hunyuan.v20230901.models.Choice;
import com.volcengine.helper.Const;
import com.volcengine.model.maas.api.Api;

import com.volcengine.service.maas.MaasService;
import com.volcengine.service.maas.impl.MaasServiceImpl;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.core.httpclient.ApacheHttpClientTransport;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;
import okhttp3.RequestBody;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin
public class ChatController {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerMessage {

        private String role;
        private String content;

    }

    @Resource
    private WebSocketService webSocketService;

    @Resource
    private ChatService chatRecordService;

    @Resource
    private LLMChatService llmChatService;

    @UserLoginToken
    @PostMapping("/send")
    public Result<Boolean> send(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "targetUserId") Long targetUserId,
            @RequestParam(value = "message") String message
    ) {
        Long currentUserId = UserInfoUtil.parseUserIdFromToken(token);
        return chatRecordService.sendTo(currentUserId, targetUserId, message);
    }

    @UserLoginToken
    @GetMapping("/record/delete")
    public Result<Boolean> deleteChatRecord(
            @RequestParam(value = "chatId") Long chatId
    ) {
        return chatRecordService.deleteChatRecord(chatId);
    }

    @UserLoginToken
    @GetMapping("/record/get")
    public Result<List<ChatRecord>> getLatestChatRecords(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", required = false) int pageNum
    ) {
         return chatRecordService.getLatestChatRecords(userId, pageSize, pageNum);
    }


    @PassToken
    @GetMapping("/send2ali/sync")
    public Result<String> send2AliYunSync(@RequestParam(value = "message") String message) {
        Constants.apiKey = "xxxx";
        Generation generation = new Generation();
        Message systemMsg = Message.builder().role(Role.SYSTEM.getValue()).content("You are a useless assistant.").build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(message).build();
        GenerationParam param = GenerationParam.builder().model("qwen-turbo").messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE).topP(0.8).build();
        GenerationResult result = null;
        try {
            result = generation.call(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<String>().success(String.valueOf(result.getOutput().getChoices().get(0).getMessage().getContent()));
    }

    @PassToken
    @GetMapping("/send2ali/stream")
    public Result<String> send2AliYunStream(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "message") String message
    )
            throws NoApiKeyException, InputRequiredException
    {
        Constants.apiKey = "xxxx";
        Generation generation = new Generation();
        Message systemMsg = Message.builder().role(Role.SYSTEM.getValue()).content("You are a useless assistant.").build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(message).build();
        GenerationParam param = GenerationParam.builder()
                .model("qwen-turbo")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8)
                .enableSearch(true)
                .incrementalOutput(true)
                .build();
        Flowable<GenerationResult> resultFlowable = generation.streamCall(param);
        StringBuilder fullContent = new StringBuilder();
        resultFlowable.blockingForEach(result -> {
            fullContent.append(result.getOutput().getChoices().get(0).getMessage().getContent());
            WebSocketService.sendMessageToUser(userId, JsonUtils.toJson(result));
            System.out.println(JsonUtils.toJson(result));
        });
        return new Result<String>().success(fullContent.toString());
    }


    @PassToken
    @GetMapping("/send2volce/sync")
    public Result<String> send2VolceSync(@RequestParam(value = "message") String message) {
        MaasService maasService = new MaasServiceImpl("maas-api.ml-platform-cn-beijing.volces.com", "cn-beijing");
        maasService.setAccessKey("xxxx");
        maasService.setSecretKey("xxxx");
        Api.ChatReq req = Api.ChatReq.newBuilder()
                .setModel(
                        Api.Model.newBuilder()
                                .setName("Skylark2-pro-4k")
                                .setVersion("1.0")
                )
                .setParameters(
                        Api.Parameters.newBuilder()
                                .setMaxNewTokens(1000)
                                .setMinNewTokens(1)
                                .setTemperature(0.7f)
                                .setTopP(0.9f)
                                .setTopK(0)
                                .setMaxPromptTokens(4000)
                )
                .addMessages(Api.Message.newBuilder().setRole(Const.MaasChatRoleOfUser).setContent(message))
                .build();
        Api.ChatResp resp = null;
        try {
            resp = maasService.chat(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<String>().success(String.valueOf(resp.getChoice().getMessage().getContent()));
    }

    private boolean xfInitialized = false;

    @PassToken
    @GetMapping("/send2xunfei/sync")
    public Result<String> send2XunfeiSync(@RequestParam(value = "message") String message) {
        if (!xfInitialized) {
            llmChatService.startConnection(123L);
            xfInitialized = true;
            return new Result<String>().success("连接刚刚建立，请再试一次");
        }
        LLMVO vo = llmChatService.sendMessage(message, 123L).getData();
        return new Result<String>().success(String.valueOf(vo.getDisplayText().get(0)));
    }

    private static final String zhiPuRequestIdTemplate = "mydemo-%d";


    @PassToken
    @GetMapping("/send2zhipu/sync")
    public Result<String> send2ZhipuSync(@RequestParam(value = "message") String message) {
        ClientV4 client = new ClientV4.Builder("xxxx")
                .build();
        String requestId = String.format(zhiPuRequestIdTemplate, System.currentTimeMillis());

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        messages.add(chatMessage);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(com.zhipu.oapi.Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .requestId(requestId)
                .messages(messages)
                .invokeMethod(com.zhipu.oapi.Constants.invokeMethod)
                .toolChoice("auto")
                .build();
        ModelApiResponse response = client.invokeModelApi(request);
        return new Result<String>().success(response.getData().getChoices().get(0).getMessage().getContent().toString());
    }

    @PassToken
    @GetMapping("/send2tencent/sync")
    public Result<String> send2TencentSync(@RequestParam(value = "message") String message) throws TencentCloudSDKException {
        String apiKey = "xxx";
        String secretKey = "xxx";
        Credential cred = new Credential(apiKey, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("hunyuan.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        HunyuanClient client = new HunyuanClient(cred, "", clientProfile);
        ChatStdRequest req = new ChatStdRequest();
        List<com.tencentcloudapi.hunyuan.v20230901.models.Message> messageList = new ArrayList<>();
        com.tencentcloudapi.hunyuan.v20230901.models.Message msg = new com.tencentcloudapi.hunyuan.v20230901.models.Message();
        msg.setRole("user");
        msg.setContent(message);
        messageList.add(msg);
        req.setMessages(messageList.toArray(new com.tencentcloudapi.hunyuan.v20230901.models.Message[0]));
        ChatStdResponse resp = client.ChatStd(req);
        Gson gson = new GsonBuilder().create();
        for (SSEResponseModel.SSE e : resp) {
            ChatStdResponse eventModel = gson.fromJson(e.Data, ChatStdResponse.class);
            Choice[] choices = eventModel.getChoices();
            if (choices.length > 0) {
                System.out.println(choices[0].getDelta().getContent());
            }
        }
        return new Result<String>().success(resp.toString());
    }

    @PassToken
    @GetMapping("/send2baidu/sync")
    public Result<String> send2BaiduSync(@RequestParam(value = "message") String message) throws IOException {
        String apiKey = "xxxx";
        String secretKey = "xxxx";
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        List<InnerMessage> messageList = new ArrayList<>();
        messageList.add(new InnerMessage("user", message));
        JSONObject bodyObject = new JSONObject();
        bodyObject.put("disable_search", false);
        bodyObject.put("enable_citation", false);
        bodyObject.put("messages", messageList);
        RequestBody body = RequestBody.create(mediaType, bodyObject.toString());
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + getAccessToken(client, apiKey, secretKey))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<String>().success(response.body().string());
    }

    private static final ThreadLocal<String> baiduTokens = new ThreadLocal<>();

    private static String getAccessToken(OkHttpClient client, String apiKey, String secretKey) throws IOException {
        if (baiduTokens.get() != null) {
            return baiduTokens.get();
        }
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + apiKey
                + "&client_secret=" + secretKey);
        OkHttpClient newClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = newClient.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            String accessToken = new JSONObject(response.body().string()).getString("access_token");
            baiduTokens.set(accessToken);
            return accessToken;
        }
        return "";
    }


}
