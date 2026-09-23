package com.xxx.myspringboot.service.impl;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.xxx.myspringboot.service.ICodeGeneratorService;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                        .entityBuilder().enableLombok())
                .templateEngine(new FreemarkerTemplateEngine())
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            // 兼容旧版本转换成Integer
                            if (JdbcType.TINYINT == metaInfo.getJdbcType()) {
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                ).execute();
    }

    protected static List<String> getTables(String tables) {
        return "*".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
