package com.example.springdemo.llm;

import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.llm.message.Message;
import com.example.springdemo.llm.message.MessageAsyncListener;
import com.example.springdemo.llm.message.MessageContext;
import com.example.springdemo.llm.protocol.IService;
import com.example.springdemo.llm.protocol.Role;
import com.example.springdemo.llm.protocol.Type;
import com.example.springdemo.service.ChatService;
import com.example.springdemo.service.WebSocketService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class LLMServiceImpl implements LLMService {

    private static final String socketEnd = "996_will_end";

    @Resource
    private ChatService chatService;

    @Resource
    private LLMCollectionService llmService;

    @Resource
    private WebSocketService webSocketService;

    @Override
    public Result<String> sendMessageSync(Long userId, String query, Type type, boolean withContext) {
        if (type == null) {
            type = Type.getDefault();
        }
        IService service = llmService.getService(type);
        if (service == null) {
            return new Result<String>().fail("service for " + type + "not found");
        }
        List<Message> queryMessages = getQueryMessageList(userId, query, withContext);
        try {
            String reply = service.invokeSync(queryMessages);
            return new Result<String>().success(reply);
        } catch (Exception e) {
            return new Result<String>().fail(e.getMessage());
        }

    }

    @Override
    public Result<String> sendMessageAsync(Long userId, String query, Type type, boolean withContext) {
        if (!webSocketService.connectedWith(userId)) {
            return new Result<String>().fail("create websocket connection with server before request stream reply");
        }
        IService service = llmService.getService(type);
        if (service == null) {
            return new Result<String>().fail("service for " + type + "not found");
        }
        List<Message> queryMessages = getQueryMessageList(userId, query, withContext);
        String reply = service.invokeAsync(queryMessages, new MessageAsyncListener() {
            @Override
            public void onStream(String messageStream) {
                webSocketService.sendToUser(userId, messageStream);
            }

            @Override
            public void onFinish() {
                webSocketService.sendToUser(userId, socketEnd);
            }
        });
        return new Result<String>().success(reply);
    }

    @Override
    public Result<Boolean> clearContext(Long userId) {
        MessageContext.getInstance().clear(userId);
        return new Result<Boolean>().success(true);
    }

    private List<Message> getQueryMessageList(Long userId, String message, boolean withContext) {
        List<Message> queryMessages = new ArrayList<>();
        if (withContext) {
            queryMessages.addAll(MessageContext.getInstance().get(userId));
        }
        Message currentMessage = new Message(Role.USER, message);
        queryMessages.add(currentMessage);
        return queryMessages;
    }

}
