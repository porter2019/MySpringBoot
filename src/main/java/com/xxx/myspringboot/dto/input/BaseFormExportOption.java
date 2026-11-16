package com.xxx.myspringboot.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 配置导出基类
 */
@Data
public class BaseFormExportOption implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 格式：excel/pdf
     */
    private String format;

    /**
     * PageQuery查询对象
     */
    private Object queryObject;
}
