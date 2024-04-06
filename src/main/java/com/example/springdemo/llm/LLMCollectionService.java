package com.example.springdemo.llm;

import com.example.springdemo.llm.protocol.IConfig;
import com.example.springdemo.llm.protocol.IService;
import com.example.springdemo.llm.protocol.Type;
import com.example.springdemo.llm.provider.TypedService;
import com.example.springdemo.llm.provider.ali.AliLLMService;
import com.example.springdemo.llm.provider.bytedance.ByteDanceLLMService;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class LLMCollectionService {

    @Resource
    private AliLLMService aliLLMService;

    @Resource
    private ByteDanceLLMService byteDanceLLMService;

    private static volatile boolean initialized = false;

    private final ConcurrentHashMap<Type, TypedService> llmServices = new ConcurrentHashMap<>();

    public synchronized void start() {
        register(aliLLMService);
        register(byteDanceLLMService);
        initServices();
    }

    public @Nullable IService getService(Type type) {
        if (!initialized) {
            start();
        }
        return llmServices.get(type);
    }

    private void register(TypedService service) {
        llmServices.put(service.provider(), service);
    }

    private void initServices() {
        for (TypedService service: llmServices.values()) {
            service.init();
        }
        initialized = true;
    }

}
