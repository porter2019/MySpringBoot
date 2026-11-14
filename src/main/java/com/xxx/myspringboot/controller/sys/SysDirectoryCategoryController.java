package com.xxx.myspringboot.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.SysDirectory.SysDirectoryCategoryModifyInput;
import com.xxx.myspringboot.entity.sys.SysDirectoryCategory;
import com.xxx.myspringboot.entity.sys.SysUser;
import com.xxx.myspringboot.service.sys.ISysDirectoryCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 字典分类管理
 */
@RestController
@RequestMapping("/sysDirectoryCategory")
@Tag(name = "字典分类")
@Validated
@PermissionHandler(module = "系统设置", handler = "字典管理", alias = "SysDirectoryCategory")
public class SysDirectoryCategoryController {

    @Resource
    private ISysDirectoryCategoryService sysDirectoryCategoryService;

    @GetMapping("get/tree")
    @Operation(summary = "获取树形列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetList(@RequestParam String search) {
        var data = sysDirectoryCategoryService.getTreeList(search);
        return ApiResult.success(data);
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = sysDirectoryCategoryService.getById(id);
        if (entity == null) {
            entity = new SysDirectoryCategory();
        }
        return ApiResult.success(entity);
    }

    @GetMapping("get/next/orderno")
    @Operation(summary = "生成排序号")
    public ApiResult GetNextOrderNo(@RequestParam @NotNull Long pid) {
        return ApiResult.success(sysDirectoryCategoryService.getNextOrderNo(pid));
    }

    @GetMapping("check/code")
    @Operation(summary = "检查编码是否存在")
    public ApiResult CheckCodeExists(@RequestParam @NotNull(message = "id 不能为空") Long id, @RequestParam @NotBlank(message = "name 不能为空") String code) {
        var wrapper = new LambdaQueryWrapper<SysDirectoryCategory>();
        wrapper.ne(id > 0, SysDirectoryCategory::getId, id);
        wrapper.eq(SysDirectoryCategory::getCode, code);
        return ApiResult.success(sysDirectoryCategoryService.exists(wrapper));
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    @PermissionAction(name = "添加", alias = "add")
    public ApiResult Add(@RequestBody @NotNull SysDirectoryCategoryModifyInput req) {
        var entity = new SysDirectoryCategory();
        BeanUtils.copyProperties(req, entity);
        sysDirectoryCategoryService.add(entity);
        return ApiResult.success("添加成功");
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    @PermissionAction(name = "修改", alias = "edit")
    public ApiResult Edit(@RequestBody @NotNull SysDirectoryCategoryModifyInput req) {
        var entity = new SysDirectoryCategory();
        BeanUtils.copyProperties(req, entity);
        sysDirectoryCategoryService.edit(entity);
        return ApiResult.success("修改成功");
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    @PermissionAction(name = "删除", alias = "delete")
    public ApiResult Delete(@RequestParam @NotNull Long id) {
        sysDirectoryCategoryService.delete(id);
        return ApiResult.success("删除成功");
    }
}
