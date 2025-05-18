package com.lzhphantom.mq;

import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CommonSendCallBack implements SendCallback {
    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("消息发送成功！消息 ID：{}", sendResult.getMsgId());
    }

    @Override
    public void onException(Throwable e) {
        log.error("消息发送失败！错误信息：{}", e.getMessage());
        throw new RuntimeException("消息发送失败");
    }
}
