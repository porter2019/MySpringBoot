package com.xxx.myspringboot.service.impl;

import com.xxx.myspringboot.entity.ContractItem;
import com.xxx.myspringboot.mapper.ContractItemMapper;
import com.xxx.myspringboot.service.IContractItemService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 合同明细 服务实现类
 */
@Service
public class ContractItemServiceImpl extends ServiceImpl<ContractItemMapper, ContractItem> implements IContractItemService {

}
