package com.xxx.myspringboot.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.service.IAttachUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 文件操作
 */
@SaCheckLogin
@RestController
@Tag(name = "文件")
@RequestMapping("/file")
public class FileController {

    @Resource
    private IAttachUploadService attachUploadService;

    @SaIgnore
    @PostMapping("upload")
    @Operation(summary = "通用文件上传")
    public ApiResult upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        var files = ((MultipartHttpServletRequest) request).getFileMap().values().stream().toList();
        String tag = request.getParameter("tag");
        if (tag == null) tag = "";

        return ApiResult.success(attachUploadService.saveAttach(files, tag));
    }

}
