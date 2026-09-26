package com.xxx.myspringboot.service.impl;

import com.xxx.myspringboot.entity.SysHandler;
import com.xxx.myspringboot.mapper.SysHandlerMapper;
import com.xxx.myspringboot.service.ISysHandlerService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 系统模块下的功能 服务实现类
 */
@Service
public class SysHandlerServiceImpl extends ServiceImpl<SysHandlerMapper, SysHandler> implements ISysHandlerService {

}
