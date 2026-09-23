package com.xxx.myspringboot.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.fun.strategy.SaCorsHandleFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.xxx.myspringboot.common.ApiResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

/**
 * sa-token的配置类
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * Sa-Token全局过滤器
     * 过滤所有请求，包括静态资源
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()

                // 指定 拦截路由 与 放行路由
                .addInclude("/**").addExclude("/favicon.ico")    /* 排除掉 /favicon.ico */


        // 认证函数: 每次请求执行
                .setAuth(obj -> {
//            var path = SaHolder.getRequest().getRequestPath();
//            System.out.println("【Sa-Token全局认证】当前请求的url：" + path);

            // 登录认证 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
            //SaRouter.match("/**", "/user/login", StpUtil::checkLogin);
            // 更多拦截处理方式，请参考“路由拦截式鉴权”章节 */
        })

                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    // 直接返回
                    //return ApiResult.error(e.getMessage());
                    // 下面改为json输出
                    // 设置响应头
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    // 使用封装的 JSON 工具类转换数据格式
                    return JSONUtil.toJsonStr(ApiResult.error(e.getMessage()));
                })

                // 前置函数：在每次认证函数之前执行（BeforeAuth 不受 includeList 与 excludeList 的限制，所有请求都会进入）
                .setBeforeAuth(r -> {
                    // ---------- 设置一些安全响应头 ----------
                    SaHolder.getResponse()
                            // 服务器名称
                            .setServer("xx-server")
                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff")
                    ;
                })
        ;
    }

    /**
     * 拦截器
     * 只拦截进入 Controller 的请求
     * 请求 → Filter（过滤器）→ DispatcherServlet → Interceptor（拦截器）→ Controller 方法
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/account/login",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",      // OpenAPI 3 文档
                        "/swagger-resources/**", // Swagger 资源
                        "/favicon.ico"
                );
    }

    /**
     * CORS 跨域处理策略
     * 请求 → Sa-Token 过滤器（或拦截器）→ Spring MVC 的跨域处理 → Controller
     * 这个过滤器在mvc跨域处理之前执行的，所以这里需要处理跨域问题
     */
    @Bean
    public SaCorsHandleFunction corsHandle() {
        return (req, res, sto) -> {
            res.setHeader("Access-Control-Allow-Origin", "*")// 允许指定域访问跨域资源
                    .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")// 允许所有请求方式
                    .setHeader("Access-Control-Max-Age", "3600")// 有效时间
                    .setHeader("Access-Control-Allow-Headers", "*");// 允许的header参数

            // 如果是预检请求，则立即返回到前端
            SaRouter.match(SaHttpMethod.OPTIONS)
                    .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                    .back();
        };
    }
}