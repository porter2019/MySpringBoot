package com.xxx.myspringboot.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * 我的字符串工具类
 */
public final class MyStringUtil {
    private MyStringUtil() {
    }

    /**
     * 将逗号分隔的字符串分割并转换为指定类型的Set集合
     *
     * @param <T>    集合中元素的类型
     * @param str    输入的逗号分隔的字符串
     * @param mapper 字符串到目标类型的转换函数
     * @return 包含转换后结果的Set集合，如果输入字符串为空则返回空集合
     */
    public static <T> Set<T> splitWithCommaToSet(String str, Function<String, T> mapper) {
        // 如果输入字符串为空或空白字符串，直接返回空集合
        if (StringUtils.isBlank(str)) {
            return Collections.emptySet();
        }
        // 将字符串按逗号分割，去除每个元素的前后空格，过滤掉空字符串，
        // 使用提供的mapper函数将每个字符串转换为指定类型，
        // 最后将结果收集到Set集合中返回
        return Arrays.stream(StringUtils.split(str, ','))
                .map(String::strip)
                .filter(StringUtils::isNotBlank)
                .map(mapper)
                .collect(Collectors.toSet());
    }

    /**
     * 将以逗号分隔的字符串转换为指定类型的List集合
     *
     * @param <T>    目标集合中元素的类型
     * @param str    输入的字符串，元素以逗号分隔
     * @param mapper 用于将字符串转换为指定类型的函数
     * @return 转换后的List集合，如果输入字符串为空则返回空List
     */
    public static <T> List<T> splitWithCommaToList(String str, Function<String, T> mapper) {
        // 如果输入字符串为空，直接返回空List
        if (StringUtils.isBlank(str)) {
            return Collections.emptyList();

        }
        // 将字符串按逗号分割为字符串数组
        // 将每个字符串元素去除前后空格
        // 过滤掉空字符串
        // 使用提供的mapper函数将每个字符串元素转换为指定类型
        // 将转换后的元素收集为List并返回
        return Arrays.stream(StringUtils.split(str, ','))
                .map(String::strip)
                .filter(StringUtils::isNotBlank)
                .map(mapper)
                .collect(Collectors.toList());
    }
}