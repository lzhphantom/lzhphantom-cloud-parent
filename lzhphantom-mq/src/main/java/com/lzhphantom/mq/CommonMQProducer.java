package com.lzhphantom.mq;

import cn.hutool.core.util.IdUtil;
import com.lzhphantom.mq.constants.CommonMQHeaderConstants;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CommonMQProducer {

    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 发送同步消息
     */
    public <T> SendResult sendSync(String topic, T msg) {
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(CommonMQHeaderConstants.KEYS, IdUtil.randomUUID())
                .build();
        return rocketMQTemplate.syncSend(topic, message);
    }

    /**
     * 发送同步消息
     */
    public <T> SendResult sendSync(String topic, String tag, T msg) {
        String destination = String.format(CommonMQHeaderConstants.DESTINATION_FORMAT, topic, tag);
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(CommonMQHeaderConstants.KEYS, IdUtil.randomUUID())
                .setHeader(CommonMQHeaderConstants.DESTINATION, destination)
                .build();
        return rocketMQTemplate.syncSend(destination, message);
    }

    /**
     * 发送异步消息
     */
    public <T> void sendAsync(String topic, T msg, SendCallback callback) {
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(CommonMQHeaderConstants.KEYS, IdUtil.randomUUID())
                .build();
        rocketMQTemplate.asyncSend(topic, message, callback);
    }

    /**
     * 发送异步消息
     */
    public <T> void sendAsync(String topic, String tag, T msg, SendCallback callback) {
        String destination = String.format(CommonMQHeaderConstants.DESTINATION_FORMAT, topic, tag);
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(CommonMQHeaderConstants.KEYS, IdUtil.randomUUID())
                .setHeader(CommonMQHeaderConstants.DESTINATION, destination)
                .build();
        rocketMQTemplate.asyncSend(destination, message, callback);
    }

    /**
     * 发送带标签的同步消息
     */
    public SendResult sendSyncWithTag(String topic, String tag, String msg) {
        String destination = String.format(CommonMQHeaderConstants.DESTINATION_FORMAT, topic, tag);
        Message<String> message = MessageBuilder.withPayload(msg)
                .setHeader(CommonMQHeaderConstants.KEYS, IdUtil.randomUUID())
                .setHeader(CommonMQHeaderConstants.DESTINATION, destination)
                .build();
        return rocketMQTemplate.syncSend(destination, message);
    }

    /**
     * 发送事务消息
     */
    public <T extends Serializable> TransactionSendResult sendTransactionalMessage(String topic, T msg, Object args) {
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(CommonMQHeaderConstants.KEYS, IdUtil.randomUUID())
                .build();
        return rocketMQTemplate.sendMessageInTransaction(topic, message, args);
    }

    /**
     * 发送事务消息
     */
    public <T> TransactionSendResult sendTransactionalMessage(String topic, String tag, T msg, Object args) {
        String destination = String.format(CommonMQHeaderConstants.DESTINATION_FORMAT, topic, tag);
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(CommonMQHeaderConstants.KEYS, IdUtil.randomUUID())
                .setHeader(CommonMQHeaderConstants.DESTINATION, destination)
                .build();
        return rocketMQTemplate.sendMessageInTransaction(destination, message, args);
    }
}
