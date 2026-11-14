package com.xxx.myspringboot.service.impl.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.xxx.myspringboot.service.common.IPDFService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements IPDFService {

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
     * pdf服务地址
     */
    @Value("${pdfService.host}")
    private String pdfServiceHost;

    /**
     * 当前站点访问域名
     */
    @Value("${app.domain-url}")
    private String domainUrl;

    private static final String BASE_ROOT_FOLDER = "uploads";


    /**
     * excel 转 pdf
     *
     * @param excelPath excel文件的物理路径
     * @param pdfPath   输出pdf的物理路径
     * @return true or false
     */
    @Override
    public Boolean excelToPdf(String excelPath, String pdfPath) {
        // 统一转成绝对路径，兼容 ./ 和 /app/wwwroot 两种写法
        Path webRoot = Paths.get(webRootPath).toAbsolutePath().normalize();

        //如果excelPath的前缀是staticUrlPrefix一致，则去掉，这个staticUrlPrefix是web访问使用的
        if (excelPath.startsWith(staticUrlPrefix)) {
            excelPath = excelPath.substring(staticUrlPrefix.length());
        }
        //如果pdfPath的前缀是staticUrlPrefix一致，则去掉，这个staticUrlPrefix是web访问使用的
        if (pdfPath.startsWith(staticUrlPrefix)) {
            pdfPath = pdfPath.substring(staticUrlPrefix.length());
        }

        //excel文件IO路径
        String excelIOPath = webRoot + excelPath;
        //pdf文件IO路径
        String pdfIOPath = webRoot + pdfPath;

        if (FileUtil.exist(excelIOPath)) {
            log.error("【pdf服务】excel文件不存在，路径：{}", excelIOPath);
            return false;
        }
        if (FileUtil.exist(pdfIOPath)) {
            //存在则删除
            FileUtil.del(pdfIOPath);
        }

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("files", FileUtil.file(excelIOPath));

        var url = pdfServiceHost + "/forms/libreoffice/convert";
        try (HttpResponse response = HttpUtil.createPost(url).form(paramMap).execute()) {
            if (!response.isOk()) {
                log.error("【pdf服务】excel转换失败，状态码：{}", response.getStatus());
                return false;
            }
            File downloadFile = FileUtil.file(pdfIOPath);
            response.writeBodyForFile(downloadFile, null);
            return true;
        }
    }
}
