package com.xxx.myspringboot.service.common;

/**
 * PDF 服务
 */
public interface IPDFService {

    /**
     * excel 转 pdf
     *
     * @param excelPath excel文件的物理路径
     * @param pdfPath   输出pdf的物理路径
     * @return true or false
     */
    Boolean excelToPdf(String excelPath, String pdfPath);
}
