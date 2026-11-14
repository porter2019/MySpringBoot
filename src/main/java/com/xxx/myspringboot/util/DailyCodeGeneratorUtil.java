package com.xxx.myspringboot.util;


import org.apache.commons.lang3.StringUtils;

/**
 * 每日代码生成工具类
 * 用于生成每日流水号，确保编号的唯一性和连续性
 */
public final class DailyCodeGeneratorUtil {
    /**
     * 私有构造函数，防止实例化工具类
     */
    private DailyCodeGeneratorUtil() { }

    /**
     * 解析当日最大编号并 +1，返回下一个流水号（补零）
     *
     * @param lastCode  数据库中查到的最新编号（可能为 null）
     * @param prefixLen 前缀长度（如 "GT20260506" 为 10）
     * @param seqLen    流水号位数（如 3）
     * @return 下一个流水号字符串（如 "001"）
     */
    public static String nextSeq(String lastCode, int prefixLen, int seqLen) {
        // 初始化下一个编号为1
        int next = 1;
        // 检查lastCode是否为空且长度是否符合要求
        if (StringUtils.isNotEmpty(lastCode) && lastCode.length() == prefixLen + seqLen) {
            try {
                // 解析当前流水号并加1
                next = Integer.parseInt(lastCode.substring(prefixLen)) + 1;
            } catch (NumberFormatException ignore) {
                // 数据异常时从 1 开始，或抛业务异常
            }
        }
        if (next > Math.pow(10, seqLen) - 1) {
            throw new IllegalStateException("当日流水号已达上限: " + seqLen + " 位");
        }
        // 返回补零后的流水号字符串
        return String.format("%0" + seqLen + "d", next);
    }
}
