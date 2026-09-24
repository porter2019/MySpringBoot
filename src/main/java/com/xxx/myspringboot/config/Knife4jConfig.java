package com.xxx.myspringboot.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Value("${sa-token.token-name}")
    private String tokenHeader;

    @Bean
    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Api接口文档")
//                        .version("1.0")
//                        .description("Spring Boot 4.x项目API接口文档")
//                        .contact(new Contact().name("X").email("dev@example.com")));

        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("API 文档").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)  // 改这里
                                        .in(SecurityScheme.In.HEADER)      // 明确放在 Header
                                        .name(tokenHeader)             // 明确参数名
                                        // 以下描述性字段可选
                                        .description("Bearer Token (格式: Bearer xxx)")
                        ));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("default")
//                .pathsToMatch("/**")
                .packagesToScan("com.xxx.myspringboot.controller") // 用点号
                .build();
    }
}