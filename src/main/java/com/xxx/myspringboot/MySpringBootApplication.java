package com.xxx.myspringboot;

import cn.dev33.satoken.SaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class MySpringBootApplication {

    public static void main(String[] args) {
//        try {
//            //session中放入实体对象需要先加到白名单里，不然无法序列化
//            SaJsonStrategy.instance.registerAllowType(User.class);
//        } catch (Exception e) {
//        }
        log.info("Sa-Token 配置如下：" + SaManager.getConfig());

        log.info("系统目录：" + System.getProperty("user.dir"));

        ConfigurableApplicationContext application = SpringApplication.run(MySpringBootApplication.class, args);
        ConfigurableEnvironment env = application.getEnvironment();

        String appName = env.getProperty("spring.application.name", "Application");
        String port = env.getProperty("server.port", "8080");

        // 获取当前激活的环境
//        log.info("spring.profiles.active property: {}", env.getProperty("spring.profiles.active"));
        String[] activeProfiles = env.getActiveProfiles();
        String profiles = activeProfiles.length > 0
                ? String.join(", ", activeProfiles)
                : "default";


        String hostAddress = "127.0.0.1";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("[警告] 无法解析本机主机名，使用默认 IP: " + hostAddress);
        }

        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Profile(s): \t{}\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Doc: \t\thttp://{}:{}/doc.html\n" +
                        "----------------------------------------------------------",
                appName, profiles, port, hostAddress, port, hostAddress, port);

    }

}
