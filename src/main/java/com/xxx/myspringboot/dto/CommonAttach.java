package com.xxx.myspringboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.myspringboot.util.AppSettingUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 通用附件
 */
@Getter
@Setter
@ToString
public class CommonAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;

    private String fileType;

    private String fileSize;

    private String fileExt;

    private String filePath;

    @JsonProperty(value = "FileWebPath", access = JsonProperty.Access.READ_ONLY)
    public String getFileWebPath() {
        if (filePath == null || filePath.isEmpty()) return "";
        return AppSettingUtil.getDomainUrl() + AppSettingUtil.getStaticUrlPrefix() + filePath;
    }

    @JsonProperty(value = "name", access = JsonProperty.Access.READ_ONLY)
    public String getName() {
        return fileName;
    }

    @JsonProperty(value = "url", access = JsonProperty.Access.READ_ONLY)
    public String getUrl() {
        return getFileWebPath();
    }

}
