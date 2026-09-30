package com.xxx.myspringboot.util;

import jakarta.servlet.http.HttpServletRequest;

public class ServletUtil {

    /**
     * 获取客户端IP地址的方法
     * 通过检查多个HTTP头信息来确定真实的客户端IP地址
     *
     * @param request HttpServletRequest对象，包含请求信息
     * @return 返回客户端的IP地址字符串
     */
    public static String getClientIp(HttpServletRequest request) {
        // 定义可能包含客户端IP的HTTP头数组
        String[] headers = {
                "X-Forwarded-For",      // 代理服务器发送的原始客户端IP
                "X-Real-IP",           // 真实客户端IP
                "Proxy-Client-IP",     // 代理客户端IP  // WebLogic代理客户端IP
                "WL-Proxy-Client-IP",      // HTTP客户端IP
                "HTTP_CLIENT_IP", // HTTP转发IP
                "HTTP_X_FORWARDED_FOR"
        };
        // 遍历所有可能的HTTP头，查找第一个有效的IP地址

        for (String header : headers) {
            // 检查IP是否有效（非空、非空字符串、非unknown）
            String ip = request.getHeader(header);
            if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For 可能是 "client, proxy1, proxy2"，取第一个
                int comma = ip.indexOf(',');
                return comma > 0 ? ip.substring(0, comma).trim() : ip.trim();
            }
        }

        String remoteAddr = request.getRemoteAddr();
        // IPv6 的本地回环地址统一显示为 127.0.0.1
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr) || "::1".equals(remoteAddr)) {
            return "127.0.0.1";
        }
        return remoteAddr;
    }
}
