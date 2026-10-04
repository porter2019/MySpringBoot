package com.xxx.myspringboot.controller.common;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.service.impl.common.MQTTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MQTT 控制器
 */
@SaCheckLogin
@RestController
@RequestMapping("/mqtt")
@Tag(name = "MQTT")
public class MQTTController {

    private final MQTTService mqttService;

    public MQTTController(MQTTService mqttService) {
        this.mqttService = mqttService;
    }

    @GetMapping("/publish")
    @Operation(summary = "发布消息")
    public ApiResult publish(String topic, String payload) {
        // mqtt未启用时
        if (mqttService == null) {
            return ApiResult.error("MQTT服务未启用");
        }
        var result = mqttService.publish(topic, payload);
        return ApiResult.success("发送结果:" + result);
    }
}
