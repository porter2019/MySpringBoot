package com.xxx.myspringboot.service.impl.common;

import com.xxx.myspringboot.handler.FitToPageHandler;
import com.xxx.myspringboot.service.common.IExportService;
import com.xxx.myspringboot.service.common.IPDFService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.fesod.sheet.FesodSheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 导出服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements IExportService {

    private final IPDFService pdfService;

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
     * 导出excel
     *
     * @param fileName 文件名,不含扩展名 abc
     * @param format   excel/pdf
     * @param head     实体类
     * @param data     数据
     * @return excel或pdf的web访问路径
     */
    @Override
    public <T> String export(String fileName, String format, Class<T> head, List<T> data) {
        // 统一转成绝对路径，兼容 ./ 和 /app/wwwroot 两种写法
        Path webRoot = Paths.get(webRootPath).toAbsolutePath().normalize();

        fileName = fileName + ".xlsx";

        // 1. 用 resolve 逐段拼接，避免手写 "/" 造成的双斜杠/漏斜杠问题
        //    BASE_ROOT_FOLDER 建议不带前后斜杠，如 "upload"
        Path saveDir = webRoot
                .resolve(BASE_ROOT_FOLDER)      // 如 webRoot/upload
                .resolve("export")
                .resolve("excel")
                .normalize();

        // 2. 文件名单独 resolve，防止 fileName 以 "/" 开头导致 resolve 返回绝对路径
        Path fileIOPath = saveDir.resolve(fileName).normalize();

        // 3. 安全校验：确保最终路径没有逃逸出 webRoot（防止 ../ 路径穿越）
        if (!fileIOPath.startsWith(webRoot)) {
            throw new IllegalArgumentException("非法的导出文件名: " + fileName);
        }

        // 判断文件夹是否存在，不存在就创建
        Path parent = fileIOPath.getParent();
        if (parent != null && !Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                log.error("【文件导出】创建文件夹失败, path={}", parent, e);
                throw new RuntimeException(e);
            }
        }

        String excelIOPath = fileIOPath.toString();
        FesodSheet.write(excelIOPath, head).registerWriteHandler(new FitToPageHandler()).sheet().doWrite(data);

        // 4. web 相对路径（用于返回 URL），用 "/" 拼接，与磁盘分隔符解耦
        String webRelativeExcel = "/" + BASE_ROOT_FOLDER + "/export/excel/" + fileName;

        if ("pdf".equalsIgnoreCase(format)) {
            // 5. PDF 路径基于 excel 的 Path 替换扩展名
            Path pdfPath = fileIOPath.resolveSibling(
                    fileName.replaceAll("\\.xlsx$", "") + ".pdf"
            );
            String pdfIOPath = pdfPath.toString();
            String webRelativePdf = "/" + BASE_ROOT_FOLDER + "/export/excel/"
                    + fileName.replaceAll("\\.xlsx$", "") + ".pdf";

            if (pdfService.excelToPdf(excelIOPath, pdfIOPath)) {
                return buildUrl(domainUrl, staticUrlPrefix, webRelativePdf);
            }
            // 转 PDF 失败，降级返回 excel
            return buildUrl(domainUrl, staticUrlPrefix, webRelativeExcel);
        }

        return buildUrl(domainUrl, staticUrlPrefix, webRelativeExcel);
    }

    /**
     * 规范化 URL 拼接，避免 domainUrl/staticUrlPrefix 缺少或重复 "/"
     */
    private String buildUrl(String domainUrl, String staticUrlPrefix, String relativePath) {
        // 1. 拆出协议头，保护其中的 "//"
        String d = domainUrl == null ? "" : domainUrl;
        String protocol = "";
        String domainRest = d;
        int idx = d.indexOf("://");
        if (idx != -1) {
            protocol = d.substring(0, idx + 3);   // 包含 "://"
            domainRest = d.substring(idx + 3);    // 协议之后的部分
        }

        // 2. 只对协议之后的部分折叠多余斜杠
        domainRest = domainRest.replaceAll("/{2,}", "/");

        // 3. 拼接 base（domainRest + staticUrlPrefix），再折叠一次路径斜杠
        String base = domainRest + "/" + (staticUrlPrefix == null ? "" : staticUrlPrefix);
        base = base.replaceAll("/{2,}", "/");
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }

        // 4. 拼路径并拼回协议
        String path = relativePath.startsWith("/") ? relativePath : "/" + relativePath;
        return protocol + base + path;
    }

}
