package com.xxx.myspringboot.controller.log;

import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.log.LogActionPageInput;
import com.xxx.myspringboot.entity.enums.ContractTypeEnum;
import com.xxx.myspringboot.service.log.ILogActionService;
import com.xxx.myspringboot.util.EnumOptionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 操作日志 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/logAction")
@Tag(name = "操作日志")
@Validated
@PermissionHandler(module = "审计日志", handler = "操作日志", alias = "LogAction")
public class LogActionController {
    @Resource
    private ILogActionService logActionService;

    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetPageList(@RequestBody LogActionPageInput req) {
        var data = logActionService.getPageList(req);
        return ApiResult.success(data).withEnums(EnumOptionUtils.buildEnumList(List.of(ContractTypeEnum.class, ContractTypeEnum.class), List.of("CType", "CType2")));
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    @PermissionAction(name = "删除", alias = "delete")
    public ApiResult Delete(@RequestParam @NotBlank String ids) {
        List<Long> idList = Arrays.stream(StringUtils.split(ids, ',')).filter(StringUtils::isNotBlank).map(x -> Long.parseLong(x.trim())).toList();

        if (idList.isEmpty()) {
            return ApiResult.failed("ids 无效");
        }

        logActionService.removeByIds(idList);

        return ApiResult.success("删除成功");
    }
}
