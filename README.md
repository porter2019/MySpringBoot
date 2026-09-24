# Spring Boot 4.1.1 快速开发框架
整合常用组件

## 生产环境下由Nginx托管静态文件
```
server {
listen 80;
server_name domain.com;

    # 静态文件直接由 Nginx 返回
    location /uploads/ {
        alias /data/myapp/wwwroot/uploads/;
        expires 30d;
        add_header Cache-Control "public";
    }

    # 其他请求转发给 Spring Boot
    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```