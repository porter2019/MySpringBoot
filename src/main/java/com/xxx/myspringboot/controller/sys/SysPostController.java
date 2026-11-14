package com.xxx.myspringboot.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.common.CurrentConst;
import com.xxx.myspringboot.dto.input.SysPost.SysPostPageInput;
import com.xxx.myspringboot.dto.output.AutoCompleteOutput;
import com.xxx.myspringboot.entity.sys.SysPost;
import com.xxx.myspringboot.entity.sys.SysRole;
import com.xxx.myspringboot.service.common.ICommonFuncService;
import com.xxx.myspringboot.service.sys.ISysPostService;
import com.xxx.myspringboot.util.MyStringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 岗位
 */
@RestController
@RequestMapping("/sysPost")
@Tag(name = "岗位")
@Validated
@PermissionHandler(module = "系统管理", handler = "岗位", alias = "SysPost")
public class SysPostController {

    @Resource
    private ISysPostService sysPostService;

    @Resource
    private ICommonFuncService commonFuncService;


    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetPageList(@RequestBody @NotNull SysPostPageInput req) {
        var data = sysPostService.getPageList(req);
        return ApiResult.success(data);
    }

    @PostMapping("/modify/sorts")
    @Operation(summary = "批量修改排序")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult ModifySorts(@RequestBody @NotNull ArrayList<Long> req) {
        if (CollectionUtils.isEmpty(req)) {
            return ApiResult.failed("数据不能为空");
        }
        for (int i = 0; i < req.size(); i++) {
            var updateWrapper = new LambdaUpdateWrapper<SysPost>();
            updateWrapper.eq(SysPost::getId, req.get(i)).set(SysPost::getSort, i + 1);
            sysPostService.update(updateWrapper);
        }
        return ApiResult.success("修改成功");
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = sysPostService.getById(id);
        if (entity == null) {
            entity = new SysPost();
        }
        return ApiResult.success(entity);
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    @PermissionAction(name = "添加", alias = "add")
    public ApiResult Add(@RequestHeader(value = CurrentConst.ClientTag, defaultValue = "") String client, @RequestBody @NotNull SysPost entity) {
        sysPostService.save(entity);
        return ApiResult.success("添加成功");
    }

    @GetMapping("check/name")
    @Operation(summary = "检查岗位名称是否存在")
    public ApiResult CheckNameExists(@RequestParam @NotNull(message = "id 不能为空") Long id, @RequestParam @NotBlank(message = "name 不能为空") String name) {
        var wrapper = new LambdaQueryWrapper<SysPost>();
        wrapper.ne(id > 0, SysPost::getId, id);
        wrapper.eq(SysPost::getName, name);
        return ApiResult.success(sysPostService.exists(wrapper));
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    @PermissionAction(name = "修改", alias = "edit")
    public ApiResult Edit(@RequestHeader(value = CurrentConst.ClientTag, defaultValue = "") String client, @RequestBody @NotNull SysPost entity) {
        sysPostService.updateById(entity);
        return ApiResult.success("修改成功");
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    @PermissionAction(name = "删除", alias = "delete")
    public ApiResult Delete(@RequestHeader(value = CurrentConst.ClientTag, defaultValue = "") String client, @RequestParam @NotBlank String ids) {
        Set<Long> idList = MyStringUtil.splitWithCommaToSet(ids, Long::parseLong);
        if (idList.isEmpty()) {
            return ApiResult.failed("ids 无效");
        }
        sysPostService.delete(idList);
        return ApiResult.success("删除成功");
    }

    @GetMapping("/switch/status")
    @Operation(summary = "修改状态")
    public ApiResult ModifyStatus(@RequestParam @NotNull Long id) {
        var model = sysPostService.getById(id);
        if (model == null) {
            return ApiResult.failed("数据不存在");
        }
        var newStatus = !model.getStatus();
        var newStatusText = newStatus ? "已启用" : "已禁用";
        var updateWrapper = new LambdaUpdateWrapper<SysPost>();
        updateWrapper.eq(SysPost::getId, id).set(SysPost::getStatus, newStatus);
        sysPostService.update(updateWrapper);
        return ApiResult.success(newStatusText);
    }

    @GetMapping("/auto/search/name")
    @Operation(summary = "自动搜索")
    public ApiResult AutoSearch(@RequestParam String keyword) {
        var wrapper = new LambdaQueryWrapper<SysPost>();
        wrapper.like(StringUtils.isNotBlank(keyword), SysPost::getName, keyword);
        wrapper.select(SysPost::getId, SysPost::getName);
        wrapper.last("limit 10");
        var result = commonFuncService.autoSearchField(sysPostService, wrapper, SysPost::getId, SysPost::getName);
        return ApiResult.success(result);
    }

}
