package ${package.Controller};

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.dto.input.${entity}PageInput;
import com.xxx.myspringboot.service.common.IExportService;
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
import tools.jackson.databind.ObjectMapper;

/**
* ${table.comment!} 控制器
*/
@Slf4j
@RestController
@RequestMapping("/${table.controllerName?lower_case?replace('controller','')}")
@Tag(name = "${table.comment!}")
@Validated
@PermissionHandler(module = 模块名称, handler = "${table.comment!}", alias = "${entity}")
public class ${table.controllerName} {

    @Resource
    private ${table.serviceName} ${entity?uncap_first}Service;

    @Resource
    private ISysDirectoryCategoryService sysDirectoryCategoryService;

    @Resource
    private IExportService exportService;

    @Resource
    private ObjectMapper objectMapper;

    @PostMapping("/get/pagelist")
    @Operation(summary = "获取分页列表")
    @PermissionAction(name = "查看", alias = "show")
    public ApiResult GetPageList(@RequestBody @NotNull ${entity}PageInput req) {
        var data = ${entity?uncap_first}Service.getPageList(req);
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
        var entity = ${entity?uncap_first}Service.getById(id);
        if (entity == null) {
            entity = new ${entity}();
        } else {
<#--            entity.setItemList(${entity}ItemService.list(new LambdaQueryWrapper<${entity}Item>().eq(${entity}Item::get${entity}Id, id).orderByAsc(${entity}Item::getSort)));-->
        }
        return ApiResult.success(entity).withEnums(buildEnums());
    }

    @PostMapping("add")
    @Operation(summary = "添加")
    @PermissionAction(name = "添加", alias = "add")
    public ApiResult Add(@RequestBody @NotNull ${entity} entity) {
<#--        for (int i = 0; i < entity.getItemList().size(); i++) {-->
<#--            entity.getItemList().get(i).setSort(i + 1);-->
<#--        }-->
        ${entity?uncap_first}Service.add(entity);
        return ApiResult.success("添加成功");
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    @PermissionAction(name = "修改", alias = "edit")
    public ApiResult Edit(@RequestBody @NotNull ${entity} entity) {
<#--        for (int i = 0; i < entity.getItemList().size(); i++) {-->
<#--            entity.getItemList().get(i).setSort(i + 1);-->
<#--        }-->
        ${entity?uncap_first}Service.edit(entity);
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

        ${entity?uncap_first}Service.removeByIds(idList);

        return ApiResult.success("删除成功");
    }

    @PostMapping("/modify/sorts")
    @Operation(summary = "批量修改排序")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult ModifySorts(@RequestBody @NotEmpty List<${entity}Item> req) {
        if (CollectionUtils.isEmpty(req)) {
            return ApiResult.failed("数据不能为空");
        }
        for (int i = 0; i < req.size(); i++) {
        LambdaUpdateWrapper<${entity}Item> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(${entity}Item::getId, req.get(i).getId()).set(${entity}Item::getSort, i + 1);
            ${entity}ItemService.update(updateWrapper);
        }
        return ApiResult.success("修改成功");
    }

    @GetMapping("/switch/status")
    @Operation(summary = "修改状态")
    public ApiResult ModifyStatus(@RequestParam @NotNull Long id) {
        var model = ${entity?uncap_first}Service.getById(id);
        if (model == null) {
            return ApiResult.failed("数据不存在");
        }
        var newStatus = !model.getStatus();
        var newStatusText = newStatus ? "已启用" : "已禁用";
        var updateWrapper = new LambdaUpdateWrapper<${entity}>();
        updateWrapper.eq(${entity}::getId, id).set(${entity}::getStatus, newStatus);
        ${entity?uncap_first}Service.update(updateWrapper);
        return ApiResult.success(newStatusText);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public ApiResult Export(@RequestBody @NotNull ${entity}PageInput req, @RequestParam @NotBlank String format) {
        req.getPageInfo().setPageIndex(1);
        req.getPageInfo().setPageSize(1000);
        var data = ${entity?uncap_first}Service.getPageList(req);
        if (ObjectUtil.isNull(data)) {
            if (data.getItems().isEmpty()) {
                return ApiResult.failed("数据为空，无法导出");
            }
        }

        var webPath = exportService.export("${entity}-" + System.currentTimeMillis(), format, ${entity}.class, data.getItems());
        return ApiResult.success(webPath);
    }

}