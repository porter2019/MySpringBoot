package com.xxx.myspringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.ContractPageInput;
import com.xxx.myspringboot.entity.Contract;
import com.xxx.myspringboot.entity.ContractItem;
import com.xxx.myspringboot.service.IContractItemService;
import com.xxx.myspringboot.service.IContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 合同表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/contract")
@Tag(name = "合同")
@Validated
@PermissionHandler(module = "演示", handler = "合同", alias = "Contract")
public class ContractController {

    @Resource
    private IContractService contractService;

    @Resource
    private IContractItemService contractItemService;

    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetPageList(@RequestBody ContractPageInput req) {
        var data = contractService.getPageList(req);
        return ApiResult.success(data);
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = contractService.getById(id);
        if (entity == null) {
            entity = new Contract();
        } else {
            entity.setItemList(contractItemService.list(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, id)));
        }
        return ApiResult.success(entity);
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    @PermissionAction(name = "添加", alias = "add")
    public ApiResult Add(@RequestBody Contract entity) {
        contractService.add(entity);
        return ApiResult.success("添加成功");
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    @PermissionAction(name = "修改", alias = "edit")
    public ApiResult Edit(@RequestBody Contract entity) {
        contractService.edit(entity);
        return ApiResult.success("修改成功");
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    @PermissionAction(name = "删除", alias = "delete")
    public ApiResult Delete(@RequestParam @NotBlank String ids) {
        List<Long> idList = Arrays.stream(StringUtils.split(ids, ','))
                .filter(StringUtils::isNotBlank)
                .map(x -> Long.parseLong(x.trim()))
                .toList();

        if (idList.isEmpty()) {
            return ApiResult.failed("ids 无效");
        }

        contractService.removeByIds(idList);

        return ApiResult.success("删除成功");
    }

}
