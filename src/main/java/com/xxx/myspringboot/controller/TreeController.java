package com.xxx.myspringboot.controller;

import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.TreeModifyInput;
import com.xxx.myspringboot.entity.Tree;
import com.xxx.myspringboot.service.ITreeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 树形数据 前端控制器
 */
@RestController
@RequestMapping("/tree")
@Tag(name = "树")
@Validated
@PermissionHandler(module = "演示", handler = "树", alias = "Tree")
public class TreeController {
    @Resource
    private ITreeService treeService;

    @GetMapping("get/tree")
    @Operation(summary = "获取树形列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetList(@RequestParam String search) {
        var data = treeService.getTreeList(search);
        return ApiResult.success(data);
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = treeService.getById(id);
        if (entity == null) {
            entity = new Tree();
        }
        return ApiResult.success(entity);
    }

    @GetMapping("get/next/orderno")
    @Operation(summary = "生成排序号")
    public ApiResult GetNextOrderNo(Long pid) {
        var orderno = treeService.getNextOrderNo(pid);
        return ApiResult.success(orderno);
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    @PermissionAction(name = "添加", alias = "add")
    public ApiResult Add(@RequestBody TreeModifyInput req) {
        var entity = new Tree();
        BeanUtils.copyProperties(req, entity);
        treeService.add(entity);
        return ApiResult.success("添加成功");
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    @PermissionAction(name = "修改", alias = "edit")
    public ApiResult Edit(@RequestBody TreeModifyInput req) {
        var entity = new Tree();
        BeanUtils.copyProperties(req, entity);
        treeService.edit(entity);
        return ApiResult.success("修改成功");
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    @PermissionAction(name = "删除", alias = "delete")
    public ApiResult Delete(@RequestParam @NotNull Long id) {
        treeService.delete(id);
        return ApiResult.success("删除成功");
    }
}
