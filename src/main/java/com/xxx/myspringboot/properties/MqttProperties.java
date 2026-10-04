package com.xxx.myspringboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private Boolean enable;
    private String brokerUrl;
    private String clientId;
    private String username;
    private String password;
    private Integer qosIn = 1;
    private Integer qosOut = 1;
    private List<String> topics = new ArrayList<>();
}