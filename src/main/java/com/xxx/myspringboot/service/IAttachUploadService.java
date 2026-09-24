package com.xxx.myspringboot.service;

import com.xxx.myspringboot.dto.UploadFileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 附件上传
 */
public interface IAttachUploadService {

    /**
     * 保存上传的附件
     * @param files 文件列表
     * @param folder 保存的文件夹
     * @return 上传后的文件数组
     */
    List<UploadFileInfo> saveAttach(List<MultipartFile> files, String folder);
}
