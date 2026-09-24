package com.xxx.myspringboot.service.impl;

import com.xxx.myspringboot.dto.UploadFileInfo;
import com.xxx.myspringboot.service.IAttachUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 文件上传服务
 */
@Service
public class AttachUploadServiceImpl implements IAttachUploadService {

    /**
     * 静态文件目录
     */
    @Value("${app.web-root-path:./}")
    private String webRootPath;

    /**
     * 静态文件访问前缀
     */
    @Value("${app.static-url-prefix}")
    private String staticUrlPrefix;

    /**
     * 当前站点访问域名
     */
    @Value("${app.domain-url}")
    private String domainUrl;

    private static final String BASE_ROOT_FOLDER = "uploads";

    /**
     * 保存附件
     *
     * @param files  上传文件列表
     * @param folder 保存的文件夹
     */
    @Override
    public List<UploadFileInfo> saveAttach(List<MultipartFile> files, String folder) {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }

        if (folder == null || folder.trim().isEmpty()) {
            folder = "attach";
        }

        // 关键：统一转成绝对路径，兼容 ./ 和 /app/wwwroot 两种写法
        Path webRoot = Paths.get(webRootPath).toAbsolutePath().normalize();

        // 对应 var baseIODirectory = App.WebHostEnvironment.WebRootPath;
        String baseIODirectory = webRootPath;

        // 拼出 uploads/xxx/yyy 的相对路径段
        Path saveFolderPath = Paths.get(BASE_ROOT_FOLDER);
        for (String part : folder.split("/")) {
            if (!part.isBlank()) saveFolderPath = saveFolderPath.resolve(part);
        }
        String saveFolder = saveFolderPath.toString().replace("\\", "/");

        List<UploadFileInfo> fileList = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElse("");
            String fileExt = getExtension(originalFilename).toLowerCase();

            if (fileExt.isEmpty() && "blob".equals(originalFilename)) fileExt = ".jpg";

            String fileName = UUID.randomUUID().toString().replace("-", "");
            String filePath = ("/" + saveFolder + "/" + fileName + fileExt).replace("\\", "/");

            // 绝对目录：webRoot + uploads/xxx/yyy
            Path targetDir = webRoot.resolve(saveFolderPath);

            try {
                Files.createDirectories(targetDir);
                Path targetFile = targetDir.resolve(fileName + fileExt);
                file.transferTo(targetFile.toFile());

                UploadFileInfo info = new UploadFileInfo();
                info.setFileExt(fileExt);
                info.setFilePath(filePath);
                info.setFileSourceName(originalFilename);
                info.setFileSize(file.getSize());
                info.setFileType(file.getContentType());
                info.setFileName(originalFilename);
                info.setFileWebPath(domainUrl + staticUrlPrefix + filePath);
                fileList.add(info);
            } catch (IOException e) {
                throw new RuntimeException("文件保存失败: " + originalFilename, e);
            }
        }

        return fileList;
    }

    /**
     * 获取文件后缀，带点。如 ".jpg"，无后缀返回 ""
     * 对应 Path.GetExtension
     */
    private String getExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) return "";
        return filename.substring(dotIndex);
    }

}
