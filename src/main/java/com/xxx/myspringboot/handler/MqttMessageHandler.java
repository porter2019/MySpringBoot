package com.xxx.myspringboot.handler;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 统一处理 MQTT消息处理器组件
 */
@Component
public class MqttMessageHandler {

    /**
     * 处理MQTT消息的方法
     * 通过@ServiceActivator注解订阅名为"mqttInputChannel"的消息通道
     * @param message 接收到的消息对象，包含消息内容和头部信息
     */
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        // 打印消息内容
        System.out.println("收到消息: " + message.getPayload());
        // 打印消息主题，从消息头部中获取
        System.out.println("主题: " + message.getHeaders().get("mqtt_receivedTopic"));
    }
}