package com.xxx.myspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.ContractPageInput;
import com.xxx.myspringboot.entity.Contract;
import com.xxx.myspringboot.entity.ContractItem;
import com.xxx.myspringboot.mapper.ContractItemMapper;
import com.xxx.myspringboot.mapper.ContractMapper;
import com.xxx.myspringboot.service.IContractService;
import com.xxx.myspringboot.service.impl.common.ItemDiffModifyService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 合同 服务实现类
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    @Resource
    private ContractItemMapper contractItemMapper;

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

        // 执行分页查询并返回结果
        return baseMapper.myPageQuery(wrapper, input.getPageInfo());
    }

    /**
     * 添加
     *
     * @param entity 包含合同信息的实体对象
     */
    @Override
    @Transactional
    public void add(Contract entity) {
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
