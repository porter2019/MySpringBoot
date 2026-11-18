package com.xxx.myspringboot.service.impl.common;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.xxx.myspringboot.service.common.IPDFService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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
        try {
            // 统一转成绝对路径（入参约定：磁盘物理路径或 web 相对路径，二选一，下面统一处理成磁盘路径）
            Path webRoot = Paths.get(webRootPath).toAbsolutePath().normalize();

            Path excelFile = toDiskPath(webRoot, excelPath);
            Path pdfFile   = toDiskPath(webRoot, pdfPath);

            if (excelFile == null || pdfFile == null) {
                log.error("【pdf服务】路径解析失败, excel={}, pdf={}", excelPath, pdfPath);
                return false;
            }

            // 1. excel 必须存在
            if (!Files.exists(excelFile)) {
                log.error("【pdf服务】excel文件不存在，路径：{}", excelFile);
                return false;
            }

            // 2. 确保 pdf 父目录存在
            Path pdfParent = pdfFile.getParent();
            if (pdfParent != null && !Files.exists(pdfParent)) {
                Files.createDirectories(pdfParent);
            }

            // 3. 已存在的 pdf 先删掉（失败记日志，不阻断）
            if (Files.exists(pdfFile)) {
                try {
                    Files.delete(pdfFile);
                } catch (IOException e) {
                    log.warn("【pdf服务】删除已存在pdf失败，路径：{}", pdfFile, e);
                    return false;
                }
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("files", excelFile.toFile());

            //docker run --name gotenberg -p 5924:3000 --restart=always --network 1panel-network -d gotenberg/gotenberg:8
            String url = pdfServiceHost + "/forms/libreoffice/convert";
            try (HttpResponse response = HttpUtil.createPost(url).form(paramMap).execute()) {
                if (!response.isOk()) {
                    log.error("【pdf服务】excel转换失败，状态码：{}", response.getStatus());
                    return false;
                }
                response.writeBodyForFile(pdfFile.toFile(), null);
                return Files.exists(pdfFile);
            }
        } catch (IOException e) {
            log.error("【pdf服务】excel转pdf异常, excel={}, pdf={}", excelPath, pdfPath, e);
            return false;
        }
    }

    /**
     * 把入参统一解析成磁盘绝对路径：
     * - 已是绝对路径且位于 webRoot 下 → 直接用
     * - 以 staticUrlPrefix 开头的 web 路径 → 去掉前缀后 resolve 到 webRoot
     * - 其他相对路径 → resolve 到 webRoot
     */
    private Path toDiskPath(Path webRoot, String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            return null;
        }
        String p = rawPath.trim().replace('\\', '/');

        // 已存在且是绝对路径：直接用
        Path direct = Paths.get(p);
        if (direct.isAbsolute()) {
            direct = direct.normalize();
            // 安全：若在 webRoot 下，直接用；否则原样返回（也可选择拒绝）
            return direct;
        }

        // 去掉 web 访问前缀（要求边界匹配，避免误伤）
        if (staticUrlPrefix != null && !staticUrlPrefix.isBlank()) {
            String prefix = staticUrlPrefix.endsWith("/")
                    ? staticUrlPrefix.substring(0, staticUrlPrefix.length() - 1)
                    : staticUrlPrefix;
            if (p.equals(prefix) || p.startsWith(prefix + "/")) {
                p = p.substring(prefix.length());
            }
        }

        // 去掉开头的 "/"，否则 resolve 会把它当绝对路径、丢掉 webRoot
        if (p.startsWith("/")) {
            p = p.substring(1);
        }
        return webRoot.resolve(p).normalize();
    }
}
