package com.xxx.myspringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.SysRole.SysRolePageInput;
import com.xxx.myspringboot.entity.SysRole;
import com.xxx.myspringboot.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 用户组
 */
@RestController
@RequestMapping("/sysRole")
@Tag(name = "用户组")
@Validated
public class SysRoleController {

    @Resource
    private ISysRoleService sysRoleService;

    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    public ApiResult GetPageList(@RequestBody SysRolePageInput req) {
        var data = sysRoleService.getPageList(req);
        return ApiResult.success(data);
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = sysRoleService.getById(id);
        if (entity == null) {
            entity = new SysRole();
        }
        return ApiResult.success(entity);
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    public ApiResult Add(@RequestBody SysRole entity) {
        entity.setIsSuper(false);
        sysRoleService.save(entity);
        return ApiResult.success("添加成功");
    }

    @GetMapping("check/name")
    @Operation(summary = "检查组名是否存在")
    public ApiResult CheckNameExists(@RequestParam @NotNull(message = "id 不能为空") Long id, @RequestParam @NotBlank(message = "name 不能为空") String name) {
        var wrapper = new QueryWrapper<SysRole>();
        wrapper.ne(id > 0, "id", id);
        wrapper.eq("name", name);
        return ApiResult.success(sysRoleService.exists(wrapper));
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    public ApiResult Edit(@RequestBody SysRole entity) {
        sysRoleService.updateById(entity);
        return ApiResult.success("修改成功");
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    public ApiResult Delete(@RequestParam @NotBlank String ids) {
        List<Long> idList = Arrays.stream(StringUtils.split(ids, ','))
                .filter(StringUtils::isNotBlank)
                .map(x -> Long.parseLong(x.trim()))
                .toList();

        if (idList.isEmpty()) {
            return ApiResult.failed("ids 无效");
        }

        sysRoleService.removeByIds(idList);

        return ApiResult.success("删除成功");
    }

}
