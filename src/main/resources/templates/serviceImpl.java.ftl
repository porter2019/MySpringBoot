package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.${entity}PageInput;
import com.xxx.myspringboot.service.common.ICommonFuncService;
import com.xxx.myspringboot.util.MyStringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
* ${table.comment!} 服务实现类
*/
@Service
@RequiredArgsConstructor
public class ${table.serviceImplName} extends ServiceImpl<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    private final ContractItemMapper contractItemMapper;

    private final ICommonFuncService commonFuncService;

    /**
    * 获取分页列表
    *
    * @param input 分页查询条件
    * @return 返回分页结果，包含列表和分页信息
    */
    @Override
    public PageResult<${entity}> getPageList(${entity}PageInput input) {
        var wrapper = new LambdaQueryWrapper<${entity}>();
        wrapper.like(StringUtils.isNotBlank(input.getName()), ${entity}::getName, input.getName());
        if (input.getFlag() != null && input.getFlag() > 0) {
            wrapper.eq(${entity}::getFlag, input.getFlag());
        }
<#--        wrapper.in(StringUtils.isNotBlank(input.getFlagIn()), ${entity}::getFlag, input.getFlagIn());-->

        var dateArr = MyStringUtil.splitWithSemicolonToSqlDateArray(input.getCreatedTime());
        if (dateArr.length == 2) {
            wrapper.ge(${entity}::getCreatedTime, dateArr[0]);// 大于等于
            wrapper.lt(${entity}::getCreatedTime, dateArr[1]);// 小于
        }

        // 执行分页查询并返回结果
        return baseMapper.myPageQuery(wrapper, input.getPageInfo());
    }

    /**
    * 生成下一个编号
    *
    * @param prefix 编号前缀 HT
    */
    @Override
    public String generateNextCode(String prefix) {
        var newPrefix = prefix + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return commonFuncService.generateNextCodeSafe(${entity}.class.getSimpleName(), newPrefix, 3, () -> {
            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime endExclusive = today.plusDays(1).atStartOfDay();
            LambdaQueryWrapper<${entity}> wrapper = new LambdaQueryWrapper<${entity}>()
                .isNotNull(${entity}::getCode)
                .ne(${entity}::getCode, "")
                .ge(${entity}::getCreatedTime, start)
                .lt(${entity}::getCreatedTime, endExclusive)
                .select(${entity}::getCode)
                .orderByDesc(${entity}::getId)
                .last("limit 1");
            var c = baseMapper.selectOne(wrapper);
            return c == null ? null : c.getCode();
        });
    }

    /**
    * 添加
    *
    * @param entity 实体对象
    */
    @Override
    @Transactional
    public void add(${entity} entity) {
        entity.setCode(generateNextCode(entity.getCType() == ${entity}TypeEnum.Buy ? "BB" : "SS"));
        baseMapper.insert(entity);
//        if (!entity.getItemList().isEmpty()) {
//            for (ContractItem item : entity.getItemList()) {
//                item.setContractId(entity.getId());
//            }
//           contractItemMapper.insert(entity.getItemList());
//        }
    }   

    /**
    * 修改
    *
    * @param entity 实体对象
    */
    @Override
    @Transactional
    public void edit(${entity} entity) {
        updateById(entity);
//        ItemDiffModifyService.sync(contractItemMapper, entity.getItemList(), entity.getId(), ContractItem::getId, ContractItem::getContractId, ContractItem::setContractId);
    }
}