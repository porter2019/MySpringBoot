package com.xxx.myspringboot.controller.log;

import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.log.LogLoginPageInput;
import com.xxx.myspringboot.entity.enums.ContractTypeEnum;
import com.xxx.myspringboot.service.log.ILogLoginService;
import com.xxx.myspringboot.util.EnumOptionUtil;
import com.xxx.myspringboot.util.MyStringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 登录日志 前端控制器
 */
@RestController
@RequestMapping("/logLogin")
@Tag(name = "登录日志")
@Validated
@PermissionHandler(module = "审计日志", handler = "登录日志", alias = "LogLogin")
public class LogLoginController {
    @Resource
    private ILogLoginService logLoginService;

    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetPageList(@RequestBody LogLoginPageInput req) {
        var data = logLoginService.getPageList(req);
        return ApiResult.success(data).withEnums(EnumOptionUtil.buildEnumList(List.of(ContractTypeEnum.class, ContractTypeEnum.class), List.of("CType", "CType2")));
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    @PermissionAction(name = "删除", alias = "delete")
    public ApiResult Delete(@RequestParam @NotBlank String ids) {
        Set<Long> idList = MyStringUtil.splitWithComma(ids, Long::parseLong);
        if (idList.isEmpty()) {
            return ApiResult.failed("ids 无效");
        }

        logLoginService.removeByIds(idList);

        return ApiResult.success("删除成功");
    }
}
