#!/bin/bash
set -e   # 任何命令失败就退出

APP_NAME=my-spring-boot
IMAGE=$APP_NAME:latest
HOST_DIR=/data/app/volumes

echo "==> 打包"
#注意给mvnw文件权限 chmod +x deploy.sh
./mvnw clean package -DskipTests

echo "==> 构建镜像"
docker build -t $IMAGE .

echo "==> 停止并删除旧容器"
docker rm -f $APP_NAME 2>/dev/null || true

echo "==> 启动新容器"
docker run --name $APP_NAME -d -p 8060:8080 \
  -v $HOST_DIR:/app/config \
  -v $HOST_DIR/wwwroot:/app/wwwroot \
  -v $HOST_DIR/logs:/app/logs \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_CONFIG_ADDITIONAL_LOCATION=file:/app/config/ \
  -e SPRING_BANNER_LOCATION=file:/app/config/banner.txt \
  -e LOG_HOME=/app/logs \
  -e LOGBACK_CONFIG=file:/app/config/logback-spring.xml \
  $IMAGE

echo "==> 完成，查看日志：docker logs -f $APP_NAME"