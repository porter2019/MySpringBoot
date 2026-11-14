package com.xxx.myspringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.ContractPageInput;
import com.xxx.myspringboot.entity.Contract;
import com.xxx.myspringboot.entity.ContractItem;
import com.xxx.myspringboot.entity.enums.ContractFlagEnum;
import com.xxx.myspringboot.entity.enums.ContractTypeEnum;
import com.xxx.myspringboot.service.IContractItemService;
import com.xxx.myspringboot.service.IContractService;
import com.xxx.myspringboot.service.common.ICommonFuncService;
import com.xxx.myspringboot.service.common.IPDFService;
import com.xxx.myspringboot.service.sys.ISysDirectoryCategoryService;
import com.xxx.myspringboot.util.EnumOptionUtil;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    @Resource
    private ISysDirectoryCategoryService sysDirectoryCategoryService;

    @Resource
    private IPDFService pdfService;

    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetPageList(@RequestBody @NotNull ContractPageInput req) {
        var data = contractService.getPageList(req);
        pdfService.excelToPdf("/uploads/export/111.xlsx","/uploads/export/111.pdf");
        return ApiResult.success(data).withEnums(buildEnums());
    }

    List<EnumOptionUtil.EnumGroupVO> buildEnums() {
        var enumList1 = EnumOptionUtil.buildEnumList(List.of(ContractTypeEnum.class, ContractFlagEnum.class), List.of("CType", "Flag"));
        var enumList2 = sysDirectoryCategoryService.buildEnumList("CarType", "Gender", "AAA");
        enumList1.addAll(enumList2);
        return enumList1;
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取详情")
    public ApiResult GetInfo(@RequestParam @NotNull Long id) {
        var entity = contractService.getById(id);
        if (entity == null) {
            entity = new Contract();
        } else {
            entity.setItemList(contractItemService.list(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, id).orderByAsc(ContractItem::getSort)));
        }
        return ApiResult.success(entity).withEnums(buildEnums());
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    @PermissionAction(name = "添加", alias = "add")
    public ApiResult Add(@RequestBody @NotNull Contract entity) {
        for (int i = 0; i < entity.getItemList().size(); i++) {
            entity.getItemList().get(i).setSort(i + 1);
        }
        contractService.add(entity);
        return ApiResult.success("添加成功");
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    @PermissionAction(name = "修改", alias = "edit")
    public ApiResult Edit(@RequestBody @NotNull Contract entity) {
        for (int i = 0; i < entity.getItemList().size(); i++) {
            entity.getItemList().get(i).setSort(i + 1);
        }
        contractService.edit(entity);
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

        contractService.removeByIds(idList);

        return ApiResult.success("删除成功");
    }

    @PostMapping("/modify/sorts")
    @Operation(summary = "批量修改排序")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult ModifySorts(@RequestBody @NotEmpty List<ContractItem> req) {
        if (CollectionUtils.isEmpty(req)) {
            return ApiResult.failed("数据不能为空");
        }
        for (int i = 0; i < req.size(); i++) {
            LambdaUpdateWrapper<ContractItem> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ContractItem::getId, req.get(i).getId()).set(ContractItem::getSort, i + 1);
            contractItemService.update(updateWrapper);
        }
        return ApiResult.success("修改成功");
    }

    @GetMapping("/modify/flag")
    @Operation(summary = "修改状态")
    public ApiResult ModifyFlag(@RequestParam @NotNull Long id, @NotNull Integer newFlag) {
        ContractFlagEnum flag = ContractFlagEnum.of(newFlag);
        LambdaUpdateWrapper<Contract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Contract::getId, id).set(Contract::getFlag, flag);
        contractService.update(updateWrapper);
        return ApiResult.success("修改成功");
    }

    @GetMapping("/switch/status")
    @Operation(summary = "修改状态")
    public ApiResult ModifyStatus(@RequestParam @NotNull Long id) {
        var model = contractService.getById(id);
        if (model == null) {
            return ApiResult.failed("数据不存在");
        }
        var newStatus = !model.getStatus();
        var newStatusText = newStatus ? "已启用" : "已禁用";
        var updateWrapper = new LambdaUpdateWrapper<Contract>();
        updateWrapper.eq(Contract::getId, id).set(Contract::getStatus, newStatus);
        contractService.update(updateWrapper);
        return ApiResult.success(newStatusText);
    }

}
