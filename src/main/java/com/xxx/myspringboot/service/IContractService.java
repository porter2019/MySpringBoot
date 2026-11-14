package com.xxx.myspringboot.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.ContractPageInput;
import com.xxx.myspringboot.entity.Contract;

/**
 * 合同 服务类
 */
public interface IContractService extends IService<Contract> {
    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    PageResult<Contract> getPageList(ContractPageInput input);

    /**
     * 生成下一个编号
     *
     * @param prefix 编号前缀 HT
     */
    String generateNextCode(String prefix);

    /**
     * 添加
     *
     * @param entity 包含合同信息的实体对象
     */
    void add(Contract entity);

    /**
     * 修改
     *
     * @param entity 包含合同信息的实体对象
     */
    void edit(Contract entity);
}
