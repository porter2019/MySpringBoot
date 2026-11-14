package com.xxx.myspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectOne;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.ContractPageInput;
import com.xxx.myspringboot.entity.Contract;
import com.xxx.myspringboot.entity.ContractItem;
import com.xxx.myspringboot.entity.enums.ContractTypeEnum;
import com.xxx.myspringboot.entity.sys.SysUserOMView;
import com.xxx.myspringboot.mapper.ContractItemMapper;
import com.xxx.myspringboot.mapper.ContractMapper;
import com.xxx.myspringboot.service.IContractService;
import com.xxx.myspringboot.service.common.ICommonFuncService;
import com.xxx.myspringboot.service.impl.common.ItemDiffModifyService;
import com.xxx.myspringboot.util.MyStringUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 合同 服务实现类
 */
@Service
@RequiredArgsConstructor
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    private final ContractItemMapper contractItemMapper;

    private final ICommonFuncService commonFuncService;

    /**
     * 获取合同分页列表
     *
     * @param input 合同分页查询条件
     * @return 返回分页结果，包含合同列表和分页信息
     */
    @Override
    public PageResult<Contract> getPageList(ContractPageInput input) {
        // 创建Lambda查询包装器
        var wrapper = new LambdaQueryWrapper<Contract>();
        // 根据合同名称进行模糊查询，如果输入名称不为空
        wrapper.like(StringUtils.isNotBlank(input.getName()), Contract::getName, input.getName());
        // 根据合同编号进行模糊查询，如果输入编号不为空
        wrapper.like(StringUtils.isNotBlank(input.getCode()), Contract::getCode, input.getCode());
        // 根据合同状态进行查询，如果输入状态不为空
        if (input.getCType() != null && input.getCType() > 0) {
            wrapper.eq(Contract::getCType, input.getCType());
        }
        // 根据合同类型进行查询，如果输入类型不为空
        if (input.getFlag() != null && input.getFlag() > 0) {
            wrapper.eq(Contract::getFlag, input.getFlag());
        }

        // flag in查询
        wrapper.in(StringUtils.isNotBlank(input.getFlagIn()), Contract::getFlag, input.getFlagIn());

        var dateArr = MyStringUtil.splitWithSemicolonToSqlDateArray(input.getCreatedTime());
        if (dateArr.length == 2) {
            wrapper.ge(Contract::getCreatedTime, dateArr[0]);// 大于等于
            wrapper.lt(Contract::getCreatedTime, dateArr[1]);// 小于
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
        return commonFuncService.generateNextCodeSafe(Contract.class.getSimpleName(), newPrefix, 3, () -> {
            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime endExclusive = today.plusDays(1).atStartOfDay();
            LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<Contract>()
                    .isNotNull(Contract::getCode)
                    .ne(Contract::getCode, "")
                    .ge(Contract::getCreatedTime, start)
                    .lt(Contract::getCreatedTime, endExclusive)
                    .select(Contract::getCode)
                    .orderByDesc(Contract::getId)
                    .last("limit 1");
            var c = baseMapper.selectOne(wrapper);
            return c == null ? null : c.getCode();
        });
    }

    /**
     * 添加
     *
     * @param entity 包含合同信息的实体对象
     */
    @Override
    @Transactional
    public void add(Contract entity) {
        entity.setCode(generateNextCode(entity.getCType() == ContractTypeEnum.Buy ? "BB" : "SS"));
        baseMapper.insert(entity);
        if (!entity.getItemList().isEmpty()) {
            for (ContractItem item : entity.getItemList()) {
                item.setContractId(entity.getId());
            }
            contractItemMapper.insert(entity.getItemList());
        }
    }

    /**
     * 修改
     *
     * @param entity 包含合同信息的实体对象
     */
    @Override
    @Transactional
    public void edit(Contract entity) {
        updateById(entity);
        ItemDiffModifyService.sync(contractItemMapper, entity.getItemList(), entity.getId(), ContractItem::getId, ContractItem::getContractId, ContractItem::setContractId);
    }

}
