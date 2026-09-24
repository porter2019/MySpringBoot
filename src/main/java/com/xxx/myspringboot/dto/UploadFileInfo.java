package com.xxx.myspringboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文件信息
 */
@Getter
@Setter
@ToString
public class UploadFileInfo {

    /**
     * 文件后缀 .jpg
     */
    private String fileExt;

    /**
     * 相对路径 /uploads/xxx/xxx.jpg
     */
    private String filePath;

    /**
     * 原始文件名
     */
    private String fileSourceName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * ContentType
     */
    private String fileType;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 完整 URL  domain + filePath
     */
    private String fileWebPath;

}