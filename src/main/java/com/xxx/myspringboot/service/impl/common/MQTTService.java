package com.xxx.myspringboot.service.impl.common;

import com.xxx.myspringboot.properties.MqttProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "mqtt", name = "enable", havingValue = "true", matchIfMissing = false)
public class MQTTService {

    private final MessageChannel mqttOutboundChannel;
    private final MqttProperties mqttProperties;

    public MQTTService(@Qualifier("mqttOutboundChannel") MessageChannel mqttOutboundChannel, MqttProperties mqttProperties) {
        this.mqttOutboundChannel = mqttOutboundChannel;
        this.mqttProperties = mqttProperties;
    }

    /**
     * 发送消息到MQTT
     *
     * @param topic   主题
     * @param payload 消息体
     */
    public Boolean publish(String topic, String payload) {
        return mqttOutboundChannel.send(
                MessageBuilder
                        .withPayload(payload)
                        .setHeader(
                                MqttHeaders.TOPIC,
                                topic
                        )
                        .setHeader(
                                MqttHeaders.QOS,
                                mqttProperties.getQosOut()
                        )
                        .build()
        );
    }
}
