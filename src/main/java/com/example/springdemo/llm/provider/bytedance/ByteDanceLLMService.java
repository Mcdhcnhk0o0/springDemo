package com.example.springdemo.llm.provider.bytedance;

import com.example.springdemo.config.ByteDanceVolceApiConfig;
import com.example.springdemo.llm.message.Message;
import com.example.springdemo.llm.message.MessageAsyncListener;
import com.example.springdemo.llm.message.TypedMessageFactory;
import com.example.springdemo.llm.protocol.Type;
import com.example.springdemo.llm.provider.TypedService;
import com.volcengine.model.maas.api.Api;
import com.volcengine.service.maas.MaasService;
import com.volcengine.service.maas.impl.MaasServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ByteDanceLLMService extends TypedService {

    private Api.Model model;
    private Api.Parameters parameters;

    private MaasService maasService;

    private final TypedMessageFactory factory = new ByteDanceMessageFactory();

    @Resource
    private ByteDanceVolceApiConfig config;

    @Override
    public void init() {
        maasService = new MaasServiceImpl("maas-api.ml-platform-cn-beijing.volces.com", "cn-beijing");
        maasService.setAccessKey(apiKey());
        maasService.setSecretKey(secretKey());
        model = Api.Model.newBuilder()
                .setName("Skylark2-pro-4k")
                .setVersion("1.0")
                .build();
        parameters = Api.Parameters.newBuilder()
                .setMaxNewTokens(1000)
                .setMinNewTokens(1)
                .setTemperature(0.7f)
                .setTopP(0.9f)
                .setTopK(0)
                .setMaxPromptTokens(4000)
                .build();
    }

    @Override
    public String invokeSync(List<Message> messages) {
        List<Api.Message> btMessageList = messages.stream()
                .map(it -> (Api.Message) factory.createTypedMessage(provider(), it))
                .collect(Collectors.toList());
        Api.ChatReq req = Api.ChatReq.newBuilder()
                .setModel(model)
                .setParameters(parameters)
                .addMessages(btMessageList.get(btMessageList.size() - 1))
                .build();
        Api.ChatResp resp = null;
        try {
            resp = maasService.chat(req);
        } catch (Exception e) {
            return e.getMessage();
        }
        return resp.getChoice().getMessage().getContent();
    }

    @Override
    public String invokeAsync(List<Message> messages, MessageAsyncListener responseListener) {
        return null;
    }

    @Override
    public String apiKey() {
        return config.getApiKey();
    }

    @Override
    public String secretKey() {
        return config.getApiSecret();
    }

    @Override
    public Type provider() {
        return Type.YUN_QUE;
    }

}
