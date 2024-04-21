package com.example.springdemo.llm.provider;

import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.llm.message.Message;
import com.example.springdemo.llm.message.MessageContext;
import com.example.springdemo.llm.protocol.Role;
import com.example.springdemo.llm.protocol.Type;
import com.example.springdemo.service.ChatService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Aspect
@Component
public class MessageAspect {

    @Resource
    private ChatService chatService;

    @Pointcut("execution(* com.example.springdemo.llm.LLMServiceImpl.sendMessageSync(..))")
    public void PointCut() {}

    @Around("PointCut()")
    public Object aroundAdviceMethod(@NotNull ProceedingJoinPoint joinPoint) {
        Object result = null;
        Object[] params = joinPoint.getArgs();
        Long userId = null;
        String query = null;
        Type type = null;
        if (params.length > 0 && params[0] instanceof Long) {
            userId = (Long) params[0];
        }
        if (params.length > 1 && params[1] instanceof String) {
            query = (String) params[1];
        }
        if (params.length > 2 && params[2] instanceof Type) {
            type = (Type) params[2];
        }
        try {
            beforeSendMessage(userId, query, type);
            result = joinPoint.proceed(joinPoint.getArgs());
            if (result instanceof Result) {
                afterReceiveMessage(userId, String.valueOf(((Result<?>) result).getData()), type);
            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return result;
    }

    private void beforeSendMessage(Long userId, String message, Type type) {
        if (message != null) {
            Message queryMessage = new Message(Role.USER, message);
            if (userId != null) {
                MessageContext.getInstance().remember(userId, queryMessage);
                if (type == null) {
                    type = Type.getDefault();
                }
                chatService.addChatRecord(userId, type.toCode(), message);
            }
        }
    }

    private void afterReceiveMessage(Long userId, String message, Type type) {
        if (message != null) {
            Message replyMessage = new Message(Role.ASSISTANT, message);
            if (userId != null) {
                MessageContext.getInstance().remember(userId, replyMessage);
                if (type == null) {
                    type = Type.getDefault();
                }
                chatService.addChatRecord(type.toCode(), userId, message);
            }
        }
    }

}
