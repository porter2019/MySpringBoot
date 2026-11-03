package com.xxx.myspringboot.controller.common;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.service.impl.common.MQTTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.util.BeanUtil;

/**
 * MQTT 控制器
 */
@SaCheckLogin
@RestController
@RequestMapping("/mqtt")
@Tag(name = "MQTT")
@RequiredArgsConstructor
public class MQTTController {

    private final ObjectProvider<MQTTService> mqttServiceProvider;

    @GetMapping("/publish")
    @Operation(summary = "发布消息")
    public ApiResult publish(String topic, String payload) {
        var mqttService = mqttServiceProvider.getIfAvailable();
        if (mqttService == null) {
            return ApiResult.error("MQTT服务未启用");
        }
        var result = mqttService.publish(topic, payload);
        return ApiResult.success("发送结果:" + result);
    }
}
