package com.example.springdemo.llm.message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class MessageContext {

    private MessageContext() {}

    public static MessageContext getInstance() {
        return InnerClass.instance;
    }

    private static final int threshold = 20;

    private final ReentrantLock lock = new ReentrantLock();

    private final Map<Long, List<Message>> messageHistory = new HashMap<>();

    public @Nonnull List<Message> get(Long userId) {
        return messageHistory.computeIfAbsent(userId, k -> new ArrayList<>());
    }

    public void remember(Long userId, Message message) {
        lock.lock();
        List<Message> history = messageHistory.computeIfAbsent(userId, k -> new ArrayList<>());
        if (history.size() > threshold) {
            history.remove(0);
        }
        history.add(message);
        lock.unlock();
    }

    public void forgetLatest(Long userId) {
        lock.lock();
        List<Message> history = messageHistory.get(userId);
        if (history != null) {
            history.remove(history.size() - 1);
        }
        lock.unlock();
    }

    public void clear(Long userId) {
        lock.lock();
        List<Message> history = messageHistory.get(userId);
        if (history != null) {
            history.clear();
        }
        lock.unlock();
    }


    private static class InnerClass {
        private static final MessageContext instance = new MessageContext();
    }

}
