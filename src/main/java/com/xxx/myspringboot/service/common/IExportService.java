package com.xxx.myspringboot.service.common;
import java.util.List;

/**
 * 导出服务
 */
public interface IExportService {

    /**
     * 导出excel
     *
     * @param fileName 文件名
     * @param format   excel/pdf
     * @param head     实体类
     * @param data     数据
     * @return 文件路径
     */
    <T> String export(String fileName, String format, Class<T> head, List<T> data);
}
