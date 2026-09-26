package com.xxx.myspringboot.service.impl;

import com.xxx.myspringboot.entity.SysModule;
import com.xxx.myspringboot.mapper.SysModuleMapper;
import com.xxx.myspringboot.service.ISysModuleService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 系统模块 服务实现类
 */
@Service
public class SysModuleServiceImpl extends ServiceImpl<SysModuleMapper, SysModule> implements ISysModuleService {

}
