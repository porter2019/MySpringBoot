package com.xxx.myspringboot.service.impl.common;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.xxx.myspringboot.service.common.ICodeGeneratorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.*;

@Service
public class CodeGeneratorServiceImpl implements ICodeGeneratorService {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void generate(String tableNames) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder.author("X").outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java"))
                .packageConfig(builder -> builder
                        .parent("com.xxx.myspringboot")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .addInclude(getTables(tableNames))
                        .entityBuilder()
                        .javaTemplate("/templates/entity.java").enableLombok()
                        .mapperBuilder().mapperTemplate("/templates/mapper.java")
                        .serviceBuilder().serviceTemplate("/templates/service.java").serviceImplTemplate("/templates/serviceImpl.java")
                        .controllerBuilder().template("/templates/controller.java")
                )
                .injectionConfig(builder -> {
                    Map<String, Object> customMap = new HashMap<>();
                    customMap.put("dtoPackage", "com.xxx.myspringboot.dto.input");
                    builder.customMap(customMap);
                    builder.customFile(new CustomFile.Builder()
                            .fileName("PageInput.java")
                            .templatePath("/templates/pageInput.java.ftl")
                            .packageName("dto.input")
                            .build());
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            String typeName = metaInfo.getTypeName().toLowerCase();
                            // tinyint(1) 通常作为布尔值使用
                            if (typeName.contains("tinyint") && typeName.contains("(1)")) {
                                return DbColumnType.BOOLEAN;
                            }
                            // datetime 映射为 LocalDateTime
                            if (typeName.contains("datetime")) {
                                return DbColumnType.LOCAL_DATE_TIME;
                            }
                            // int 映射为 Integer
                            if (typeName.equals("int") || typeName.equals("integer")) {
                                return DbColumnType.INTEGER;
                            }
                            // decimal 映射为 BigDecimal
                            if (typeName.contains("decimal")) {
                                return DbColumnType.BIG_DECIMAL;
                            }
                            // 其余走默认转换
                            return typeRegistry.getColumnType(metaInfo);
                        })
                ).execute();
    }

    protected static List<String> getTables(String tables) {
        return "*".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
