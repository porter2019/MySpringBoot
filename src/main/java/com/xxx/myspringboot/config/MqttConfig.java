package com.xxx.myspringboot.config;

import com.xxx.myspringboot.properties.MqttProperties;
import jakarta.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * MQTT配置类
 * 用于配置和初始化MQTT客户端的相关组件
 */
@Configuration
@EnableIntegration
@EnableConfigurationProperties(MqttProperties.class)
@ConditionalOnProperty(prefix = "mqtt", name = "enable", havingValue = "true", matchIfMissing = false)
public class MqttConfig {

    @Resource
    private MqttProperties mqttProperties;

    /**
     * 创建MQTT客户端工厂
     *
     * @return MqttPahoClientFactory MQTT客户端工厂实例
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置MQTT服务器地址
        options.setServerURIs(new String[]{mqttProperties.getBrokerUrl()});
        // 设置用户名
        options.setUserName(mqttProperties.getUsername());
        // 设置密码
        options.setPassword(mqttProperties.getPassword().toCharArray());
        // 清除会话
        options.setCleanSession(true);
        // 自动重连
        options.setAutomaticReconnect(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 入站适配器（订阅主题）
     *
     * @return 入站适配器实例
     */
    @Bean
    public MessageProducerSupport mqttInbound() {
        System.out.print("[MQTT]订阅的topic:" + mqttProperties.getTopics().toString());
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getClientId() + "-in",
                        mqttClientFactory(), mqttProperties.getTopics().toArray(new String[0]));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(mqttProperties.getQosIn());
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * 创建一个名为"mqttInputChannel"的Bean，用于处理MQTT消息的输入通道
     *
     * @return MessageChannel 返回一个DirectChannel类型的消息通道
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        // 创建并返回一个DirectChannel实例，用于消息的传递和路由
        return new DirectChannel();
    }


    /*
     * 出站适配器（发布消息）
     * 这个方法用于配置MQTT出站消息处理器，负责将消息发布到MQTT代理服务器
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")  // 指定输入通道为"mqttOutboundChannel"
    public MessageHandler mqttOutbound() {
        // 创建MQTT Paho消息处理器，客户端ID为配置中的clientId加上"-out"后缀
        MqttPahoMessageHandler handler =
                new MqttPahoMessageHandler(mqttProperties.getClientId() + "-out", mqttClientFactory());
        // 设置为异步处理模式
        handler.setAsync(true);
        // 设置默认发布主题为"testtopic/xxx"
        handler.setDefaultTopic("testtopic/xxx");
        // 设置默认QoS（服务质量等级）为1，至少一次交付保证
        handler.setDefaultQos(mqttProperties.getQosOut());
        // 返回配置好的消息处理器
        return handler;
    }

    /**
     * 配置MQTT输出消息通道
     * 该方法用于创建一个名为"mqttOutboundChannel"的Bean，它是一个DirectChannel类型的消息通道
     * DirectChannel是Spring Integration中的一种点对点消息通道，它会将消息发送到单一订阅者
     *
     * @return MessageChannel 返回一个DirectChannel实例，用于发送MQTT消息
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
}