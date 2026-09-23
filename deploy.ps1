.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t my-spring-boot:latest .
docker rm -f my-spring-boot 2>$null
docker run --name my-spring-boot -d -p 8060:8080 -v D:\WorkSpace\Java\demo1\vol:/app/config -e SPRING_PROFILES_ACTIVE=prod -e SPRING_CONFIG_ADDITIONAL_LOCATION=file:/app/config/ -e SPRING_BANNER_LOCATION=file:/app/config/banner.txt my-spring-boot:latest