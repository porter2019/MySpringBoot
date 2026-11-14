package com.xxx.myspringboot.controller.sys;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.SysDirectory.SysDirectoryCategoryModifyInput;
import com.xxx.myspringboot.dto.input.SysOrg.SysOrgModifyInput;
import com.xxx.myspringboot.entity.sys.SysDirectoryCategory;
import com.xxx.myspringboot.entity.sys.SysOrg;
import com.xxx.myspringboot.entity.sys.SysUser;
import com.xxx.myspringboot.service.sys.ISysOrgService;
import com.xxx.myspringboot.service.sys.ISysUserService;
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
 * 部门
 */
@RestController
@RequestMapping("/sysOrg")
@Tag(name = "部门")
@Validated
@PermissionHandler(module = "系统设置", handler = "部门管理", alias = "SysOrg")
public class SysOrgController {

    @Resource
    private ISysOrgService sysOrgService;

    @Resource
    private ISysUserService sysUserService;

    @GetMapping("get/tree")
    @Operation(summary = "获取树形列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetList(@RequestParam String search) {
        var data = sysOrgService.getTreeList(search);
        return ApiResult.success(data);
    }

    @GetMapping("modify/leader")
    @Operation(summary = "修改部门经理")
    public ApiResult ModifyLeader(@RequestParam @NotNull Long id, @RequestParam @NotNull Long leaderId) {
        var model = sysOrgService.getById(id);
        if (model == null) return ApiResult.failed("部门不存在");
        var userModel = sysUserService.getById(leaderId);
        if (userModel == null) return ApiResult.failed("用户不存在");

        var updateWrapper = new LambdaUpdateWrapper<SysOrg>();
        updateWrapper.eq(SysOrg::getId, id).set(SysOrg::getLeaderId, leaderId).set(SysOrg::getLeaderName, userModel.getUserName());
        sysOrgService.update(updateWrapper);

        return ApiResult.success("修改成功");
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = sysOrgService.getById(id);
        if (entity == null) {
            entity = new SysOrg();
        }
        return ApiResult.success(entity);
    }

    @GetMapping("get/next/orderno")
    @Operation(summary = "生成排序号")
    public ApiResult GetNextOrderNo(@RequestParam @NotNull Long pid) {
        return ApiResult.success(sysOrgService.getNextOrderNo(pid));
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    @PermissionAction(name = "添加", alias = "add")
    public ApiResult Add(@RequestBody @NotNull SysOrgModifyInput req) {
        var entity = new SysOrg();
        BeanUtils.copyProperties(req, entity);
        sysOrgService.add(entity);
        return ApiResult.success("添加成功");
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    @PermissionAction(name = "修改", alias = "edit")
    public ApiResult Edit(@RequestBody @NotNull SysOrgModifyInput req) {
        var entity = new SysOrg();
        BeanUtils.copyProperties(req, entity);
        sysOrgService.edit(entity);
        return ApiResult.success("修改成功");
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    @PermissionAction(name = "删除", alias = "delete")
    public ApiResult Delete(@RequestParam @NotNull Long id) {
        sysOrgService.delete(id);
        return ApiResult.success("删除成功");
    }
}
