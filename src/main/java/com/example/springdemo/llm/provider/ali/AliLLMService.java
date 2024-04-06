package com.example.springdemo.llm.provider.ali;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.utils.Constants;
import com.alibaba.dashscope.utils.JsonUtils;
import com.example.springdemo.config.AliCloudApiConfig;
import com.example.springdemo.llm.message.Message;
import com.example.springdemo.llm.message.MessageAsyncListener;
import com.example.springdemo.llm.message.TypedMessageFactory;
import com.example.springdemo.llm.protocol.Type;
import com.example.springdemo.llm.provider.TypedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class AliLLMService extends TypedService {

    private Generation generation;

    @Resource
    private AliCloudApiConfig config;

    @Override
    public void init() {
        Constants.apiKey = apiKey();
        generation = new Generation();
    }

    @Override
    public String invokeSync(List<Message> messages) {
        final TypedMessageFactory factory = new AliMessageFactory();
        List<com.alibaba.dashscope.common.Message> aliMessageList = new ArrayList<>();
        messages.forEach(message -> aliMessageList.add(
                (com.alibaba.dashscope.common.Message) factory.createTypedMessage(provider(), message)
        ));
        GenerationParam param = GenerationParam.builder()
                .model("qwen-turbo")
                .messages(aliMessageList)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8)
                .build();
        GenerationResult result = null;
        try {
            result = generation.call(param);
        } catch (Exception e) {
            return e.getMessage();
        }
        return result.getOutput().getChoices().get(0).getMessage().getContent();
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
        return null;
    }

    @Override
    public Type provider() {
        return Type.TONG_YI;
    }
}
