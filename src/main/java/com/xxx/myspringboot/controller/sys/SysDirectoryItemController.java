package com.xxx.myspringboot.controller.sys;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.SysDirectory.SysDirectoryItemFullViewPageInput;
import com.xxx.myspringboot.entity.sys.SysDirectoryItem;
import com.xxx.myspringboot.entity.sys.SysDirectoryItemFullView;
import com.xxx.myspringboot.entity.sys.SysPost;
import com.xxx.myspringboot.service.sys.ISysDirectoryItemFullViewService;
import com.xxx.myspringboot.service.sys.ISysDirectoryItemService;
import com.xxx.myspringboot.util.MyStringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

/**
 * 字典项 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/sysDirectoryItem")
@Tag(name = "字典项")
@Validated
//@PermissionHandler(module = "演示", handler = "合同", alias = "SysDirectoryItem")
public class SysDirectoryItemController {

    @Resource
    private ISysDirectoryItemService sysDirectoryItemService;

    @Resource
    private ISysDirectoryItemFullViewService sysDirectoryItemFullViewService;

    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetPageList(@RequestBody @NotNull SysDirectoryItemFullViewPageInput req) {
        var data = sysDirectoryItemFullViewService.getPageList(req);
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
            var updateWrapper = new LambdaUpdateWrapper<SysDirectoryItem>();
            updateWrapper.eq(SysDirectoryItem::getId, req.get(i)).set(SysDirectoryItem::getSort, i + 1);
            sysDirectoryItemService.update(updateWrapper);
        }
        return ApiResult.success("修改成功");
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = sysDirectoryItemFullViewService.getById(id);
        if (entity == null) {
            entity = new SysDirectoryItemFullView();
        }
        return ApiResult.success(entity);
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    @PermissionAction(name = "添加", alias = "add")
    public ApiResult Add(@RequestBody @NotNull SysDirectoryItem entity) {
        sysDirectoryItemService.save(entity);
        return ApiResult.success("添加成功");
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    @PermissionAction(name = "修改", alias = "edit")
    public ApiResult Edit(@RequestBody @NotNull SysDirectoryItem entity) {
        sysDirectoryItemService.updateById(entity);
        return ApiResult.success("修改成功");
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除")
    @PermissionAction(name = "删除", alias = "delete")
    public ApiResult Delete(@RequestParam @NotBlank String ids) {
        Set<Long> idList = MyStringUtil.splitWithCommaToSet(ids, Long::parseLong);
        if (idList.isEmpty()) {
            return ApiResult.failed("ids 无效");
        }

        sysDirectoryItemService.removeByIds(idList);

        return ApiResult.success("删除成功");
    }

    @GetMapping("/switch/status")
    @Operation(summary = "修改状态")
    public ApiResult ModifyStatus(@RequestParam @NotNull Long id) {
        var model = sysDirectoryItemService.getById(id);
        if (model == null) {
            return ApiResult.failed("数据不存在");
        }
        var newStatus = !model.getStatus();
        var newStatusText = newStatus ? "已启用" : "已禁用";
        LambdaUpdateWrapper<SysDirectoryItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDirectoryItem::getId, id).set(SysDirectoryItem::getStatus, newStatus);
        sysDirectoryItemService.update(updateWrapper);
        return ApiResult.success(newStatusText);
    }
}
