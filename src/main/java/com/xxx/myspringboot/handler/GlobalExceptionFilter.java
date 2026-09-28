package com.xxx.myspringboot.handler;

import com.xxx.myspringboot.common.ApiResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 全局异常过滤器类
 * 用于统一处理应用程序中抛出的各种异常
 * 提供全局异常捕获和响应机制
 * RestControllerAdvice 处理不了 Filter 抛出的异常，需要单独包一层。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    /**
     * 重写父类的doFilterInternal方法，实现过滤器核心逻辑
     *
     * @param request  HttpServletRequest对象，包含客户端请求信息
     * @param response HttpServletResponse对象，用于向客户端返回响应
     * @param chain    FilterChain对象，用于调用过滤器链中的下一个过滤器
     * @throws ServletException 可能抛出的Servlet异常
     * @throws IOException      可能抛出的IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            // 执行过滤器链中的下一个过滤器
            chain.doFilter(request, response);
        } catch (Throwable e) {
            // 捕获所有异常并记录日志，包含请求URI和异常堆栈信息
            log.error("Filter 系统异常 uri={}", request.getRequestURI(), e);
            // 向客户端返回错误响应，状态码500，提示信息"系统繁忙，请稍后重试"
            writeError(response, 500, "系统繁忙，请稍后重试");
        }
    }

    /**
     * 写入错误响应信息到HTTP响应中
     *
     * @param response HTTP响应对象
     * @param code     HTTP状态码
     * @param msg      错误信息
     * @throws IOException 如果发生I/O错误
     */
    private void writeError(HttpServletResponse response, int code, String msg) throws IOException {
        if (response.isCommitted()) {
            // 响应已提交（如流式输出中），无法再写
            log.warn("响应已提交，无法写入错误信息: {}", msg);
            return;
        }
        response.reset(); // 重置响应，清除之前的任何内容
        response.setStatus(code == 0 ? 500 : code); // 设置HTTP状态码，如果code为0则使用500
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置内容类型为JSON
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 设置字符编码为UTF-8
        // 将错误信息序列化为JSON字符串并写入响应输出流
        response.getWriter().write(objectMapper.writeValueAsString(ApiResult.error(code, msg)));
    }
}
