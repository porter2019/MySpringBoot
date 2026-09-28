# ========== 变量定义 ==========
$VOLUMES   = "D:\WorkSpace\Java\MySpringBoot\volumes"
$CONFIG    = "$VOLUMES"
$WWWROOT   = "$VOLUMES\wwwroot"
$UPLOADS   = "$VOLUMES\wwwroot\uploads"
$LOGS      = "$VOLUMES\logs"
$PORT      = 8060

# ========== 创建目录 ==========
New-Item -ItemType Directory -Path $UPLOADS -Force | Out-Null
New-Item -ItemType Directory -Path $LOGS    -Force | Out-Null

# ========== 打包 ==========
.\mvnw.cmd clean package -DskipTests

# ========== 构建镜像 ==========
docker build -t my-spring-boot:latest .
#docker build --no-cache -t my-spring-boot:latest .

# ========== 删除旧容器 ==========
docker rm -f my-spring-boot 2>$null
$LASTEXITCODE = 0

# ========== 启动容器 ==========
docker run --name my-spring-boot -d -p ${PORT}:8080 `
  -v "${CONFIG}:/app/config" `
  -v "${WWWROOT}:/app/wwwroot" `
  -v "${LOGS}:/app/logs" `
  -e SPRING_PROFILES_ACTIVE=prod `
  -e SPRING_CONFIG_ADDITIONAL_LOCATION=file:/app/config/ `
  -e SPRING_BANNER_LOCATION=file:/app/config/banner.txt `
  -e LOG_HOME=/app/logs `
  -e LOGBACK_CONFIG=file:/app/config/logback-spring.xml `
  my-spring-boot:latest

#Read-Host "按 Enter 键退出"

for ($i = 5; $i -gt 0; $i--) {
  Write-Host "还有 $i 秒退出..."
  Start-Sleep 1
}