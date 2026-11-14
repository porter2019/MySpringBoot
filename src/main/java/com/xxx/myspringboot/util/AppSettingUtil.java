package com.xxx.myspringboot.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置文件工具类
 */
@Component
public class AppSettingUtil {

    //region 域名

    private static String domainUrl;

    @Value("${app.domain-url:}")
    public void setDomainUrl(String url) {
        domainUrl = url;
    }

    public static String getDomainUrl() {
        return domainUrl;
    }

    //endregion

    //region 静态资源访问路径
    private static String staticUrlPrefix;

    @Value("${app.static-url-prefix:}")
    public String setStaticUrlPrefix(String prefix) {
        return staticUrlPrefix = prefix;
    }

    public static String getStaticUrlPrefix() {
        return staticUrlPrefix;
    }

    //endregion
}
