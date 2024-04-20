package com.example.springdemo.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component
public class RabbitMqReceiver {

    @RabbitListener(queues = "topicQueue")
    public void receiveTopicMessage(Map<String, Object> message) {
        log.info("receive message in topic queue: " + message.toString());
    }

}
