package com.xxx.myspringboot.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Slf4j
@Component
public class Knife4jOperationCustomizer implements GlobalOperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (operation.getSecurity() == null) {
            operation.setSecurity(List.of(new SecurityRequirement().addList("bearerAuth")));
        }
        return operation;
    }
}
