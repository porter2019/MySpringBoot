package com.xxx.myspringboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.common.CurrentConst;
import com.xxx.myspringboot.common.PublicConst;
import com.xxx.myspringboot.dto.input.SysUser.SysUserPageInput;
import com.xxx.myspringboot.entity.SysUser;
import com.xxx.myspringboot.entity.SysUserOMView;
import com.xxx.myspringboot.service.ISysRoleUserService;
import com.xxx.myspringboot.service.ISysUserOMViewService;
import com.xxx.myspringboot.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sysUser")
@Tag(name = "系统用户")
@Validated
public class SysUserController {

    @Resource
    private ISysUserOMViewService sysUserOMViewService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysRoleUserService sysRoleUserService;

    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    public ApiResult GetPageList(@RequestBody SysUserPageInput req) {
        var data = sysUserOMViewService.getPageList(req);
        data.getItems().forEach(item -> {
            item.setPassword(PublicConst.Display_User_Password);
        });
        return ApiResult.success(data);
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = sysUserOMViewService.getById(id);
        if (entity == null) {
            entity = new SysUserOMView();
        } else {
            entity.setPassword(PublicConst.Display_User_Password);
        }
        return ApiResult.success(entity);
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    public ApiResult Add(@RequestBody SysUserOMView entity) {
        var id = sysUserService.Add(entity);
        return ApiResult.success(id > 0 ? "添加成功" : "添加失败");
    }

    @GetMapping("check/cellphone")
    @Operation(summary = "检查手机号是否存在")
    public ApiResult CheckCelPhoneExists(@RequestParam @NotNull(message = "id 不能为空") Long id, @RequestParam @NotBlank(message = "name 不能为空") String cellPhone) {
        var wrapper = new QueryWrapper<SysUser>();
        wrapper.ne(id > 0, "id", id);
        wrapper.eq("cell_phone", cellPhone); //数据库中的字段名
        return ApiResult.success(sysUserService.exists(wrapper));
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    public ApiResult Edit(@RequestBody SysUserOMView entity) {
        sysUserService.Edit(entity);
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

        var isOK = sysUserService.Delete(idList);
        return ApiResult.success(isOK ? "删除成功" : "删除失败");
    }

    @GetMapping("switch/status")
    @Operation(summary = "修改状态")
    public ApiResult ChangeStatus(@RequestParam @NotNull Long id) {
        var entity = sysUserService.getById(id);
        if (entity == null) {
            return ApiResult.failed("数据不存在");
        }
        var newStatus = !entity.getStatus();
        var newStatusText = newStatus ? "已启用" : "已禁用";

        var updateWrapper = new LambdaUpdateWrapper<SysUser>();
        updateWrapper.eq(SysUser::getId, entity.getId())
                .set(SysUser::getStatus, newStatus)
                .set(SysUser::getUpdatedUserId, StpUtil.getLoginIdAsLong())
                .set(SysUser::getUpdatedUserName, StpUtil.getSession().get(CurrentConst.UserName))
                .set(SysUser::getUpdatedTime, LocalDateTime.now());
        sysUserService.update(updateWrapper);

        return  ApiResult.success(newStatusText);
    }

    @GetMapping("switch/isom")
    @Operation(summary = "修改后台权限状态")
    public ApiResult ChangeIsOM(@RequestParam @NotNull Long id) {
        var entity = sysUserService.getById(id);
        if (entity == null) {
            return ApiResult.failed("数据不存在");
        }
        var newStatus = !entity.getIsOM();
        var newStatusText = newStatus ? "已启用" : "已禁用";

        var updateWrapper = new LambdaUpdateWrapper<SysUser>();
        updateWrapper.eq(SysUser::getId, entity.getId())
                .set(SysUser::getIsOM, newStatus)
                .set(SysUser::getUpdatedUserId, StpUtil.getLoginIdAsLong())
                .set(SysUser::getUpdatedUserName, StpUtil.getSession().get(CurrentConst.UserName))
                .set(SysUser::getUpdatedTime, LocalDateTime.now());
        sysUserService.update(updateWrapper);

        return  ApiResult.success(newStatusText);
    }

    @GetMapping("switch/ismp")
    @Operation(summary = "修改后台权限状态")
    public ApiResult ChangeIsMP(@RequestParam @NotNull Long id) {
        var entity = sysUserService.getById(id);
        if (entity == null) {
            return ApiResult.failed("数据不存在");
        }
        var newStatus = !entity.getIsMP();
        var newStatusText = newStatus ? "已启用" : "已禁用";

        var updateWrapper = new LambdaUpdateWrapper<SysUser>();
        updateWrapper.eq(SysUser::getId, entity.getId())
                .set(SysUser::getIsMP, newStatus)
                .set(SysUser::getUpdatedUserId, StpUtil.getLoginIdAsLong())
                .set(SysUser::getUpdatedUserName, StpUtil.getSession().get(CurrentConst.UserName))
                .set(SysUser::getUpdatedTime, LocalDateTime.now());
        sysUserService.update(updateWrapper);

        return  ApiResult.success(newStatusText);
    }

}
