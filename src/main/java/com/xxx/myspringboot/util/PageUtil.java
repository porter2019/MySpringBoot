package com.xxx.myspringboot.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.PageOptions;
import org.apache.commons.lang3.StringUtils;

/**
 * 分页帮助类
 */
public final class PageUtil {

    private PageUtil() {
    }

    /** 默认页码 */
    private static final int DEFAULT_PAGE_INDEX = 1;
    /** 默认页容量 */
    private static final int DEFAULT_PAGE_SIZE = 10;
    /** 最大页容量，防止恶意请求 */
    private static final int MAX_PAGE_SIZE = 500;

    /**
     * 根据 PageOptions 构建 MyBatis-Plus 的 Page 对象
     */
    public static <T> Page<T> buildPage(PageOptions options) {
        int pageIndex = (options == null || options.getPageIndex() == null || options.getPageIndex() < 1)
                ? DEFAULT_PAGE_INDEX : options.getPageIndex();
        int pageSize = (options == null || options.getPageSize() == null || options.getPageSize() < 1)
                ? DEFAULT_PAGE_SIZE : Math.min(options.getPageSize(), MAX_PAGE_SIZE);

        Page<T> page = new Page<>(pageIndex, pageSize);

        // 解析排序字段，格式： "createTime desc,id asc" 或 "createTime:desc"
        if (options != null && StringUtils.isNotBlank(options.getOrderBy())) {
            String orderBy = options.getOrderBy().trim();
            String[] items = orderBy.split(",");
            for (String item : items) {
                String[] parts = item.trim().split("\\s+|:");
                if (parts.length == 0 || StringUtils.isBlank(parts[0])) {
                    continue;
                }
                String column = camelToUnderline(parts[0].trim());
                boolean isAsc = parts.length < 2 || !"desc".equalsIgnoreCase(parts[1].trim());
                page.addOrder(isAsc ? OrderItem.asc(column) : OrderItem.desc(column));
            }
        }
        return page;
    }

    /**
     * 将 MyBatis-Plus 的 Page 转换为统一返回结构 PageResult
     */
    public static <T> PageResult<T> toResult(Page<T> page) {
        return new PageResult<>(
                (int) page.getCurrent(),
                (int) page.getSize(),
                page.getTotal(),
                page.getRecords()
        );
    }

    /**
     * 驼峰转下划线，防止 SQL 注入（只保留字母数字下划线）
     */
    private static String camelToUnderline(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        // 过滤非法字符，防止 SQL 注入
        String safe = name.replaceAll("[^a-zA-Z0-9_]", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < safe.length(); i++) {
            char c = safe.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}