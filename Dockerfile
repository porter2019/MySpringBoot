# 使用轻量级 JRE 基础镜像，减小体积
FROM eclipse-temurin:21-jre-noble

# 设置工作目录
WORKDIR /app

# 将本地 JAR 复制到容器中并重命名
COPY target/MySpringBoot-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用端口（需与 application.properties 一致）
EXPOSE 8080

# 设置启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]