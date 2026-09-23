package com.xxx.myspringboot.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.http.server.HttpServerRequest;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.service.ICodeGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统接口
 */
@SaCheckLogin
@RestController
@RequestMapping("/sys")
@Tag(name = "系统")
public class SysController {

    @Resource
    private ICodeGeneratorService codeGeneratorService;

    @Autowired
    private Environment env;

    /**
     * 根据数据库的表生成实体
     *
     * @param tableNames 全部表填*，多个英文逗号分隔
     */
    @SaIgnore
    @GetMapping("/db/sync")
    @Operation(summary = "根据数据库的表生成实体")
    public ApiResult CodeGenerator(@RequestParam(defaultValue = "") String tableNames) {
        if (tableNames.isBlank()) {
            return ApiResult.error("缺少参数");
        }
        codeGeneratorService.generate(tableNames);
        return ApiResult.success();
    }


//    @SaIgnore
//    @GetMapping("/get/config")
//    @Operation(summary = "获取配置文件中的值")
//    public ApiResult GetConfigValue(@RequestParam(defaultValue = "spring.profiles.active") String path) {
//        if (path.isBlank()) {
//            return ApiResult.error("缺少路径");
//        }
//
//        var val = env.getProperty(path);
//
//        return ApiResult.success(val);
//    }

    @SaIgnore
    @GetMapping("/get/ip")
    @Operation(summary = "获取IP信息")
    public ApiResult GetIP(HttpServletRequest  request) {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("env", env.getProperty("spring.profiles.active"));

        // ---------- 客户端信息 ----------
        data.put("clientIp", getClientIp(request));
        data.put("clientHost", request.getRemoteHost());
        data.put("clientPort", request.getRemotePort());
        data.put("clientForwardedIps", getForwardedIps(request));

        // ---------- 请求信息 ----------
        data.put("requestUrl", request.getRequestURL().toString());
        data.put("requestUri", request.getRequestURI());
        data.put("method", request.getMethod());
        data.put("scheme", request.getScheme());
        data.put("userAgent", request.getHeader("User-Agent"));
        data.put("serverPort", request.getServerPort());
        data.put("serverName", request.getServerName());

        // ---------- 服务器信息 ----------
        data.put("serverIp", getServerIp());
        data.put("hostName", getHostName());

        // ---------- 代理链路（可能为空）----------
        data.put("xForwardedFor", request.getHeader("X-Forwarded-For"));
        data.put("xRealIp", request.getHeader("X-Real-IP"));
        data.put("xForwardedProto", request.getHeader("X-Forwarded-Proto"));

        return ApiResult.success(data);
    }

    //region 私有方法

    /**
     * 获取客户端真实 IP。
     * 依次检查常见代理头，最后回退到 request.getRemoteAddr()。
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
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

    /**
     * 获取完整的代理转发链路。
     */
    private List<String> getForwardedIps(HttpServletRequest request) {
        List<String> ips = new ArrayList<>();
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            for (String ip : xff.split(",")) {
                String trimmed = ip.trim();
                if (!trimmed.isEmpty()) {
                    ips.add(trimmed);
                }
            }
        }
        return ips;
    }

    /**
     * 获取服务器本机 IP（非回环）。
     */
    private String getServerIp() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    /**
     * 获取服务器主机名。
     */
    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    /**
     * 敏感值脱敏：保留前 2 位和后 2 位，中间用 * 代替。
     */
    private String maskValue(String val) {
        if (val == null || val.length() <= 4) {
            return "****";
        }
        int keep = 2;
        return val.substring(0, keep)
                + "*".repeat(val.length() - keep * 2)
                + val.substring(val.length() - keep);
    }

    //endregion

}
