package com.xxx.myspringboot.service.impl.sys;

import com.xxx.myspringboot.entity.sys.SysRolePermit;
import com.xxx.myspringboot.mapper.sys.SysRolePermitMapper;
import com.xxx.myspringboot.service.sys.ISysRolePermitService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户组所拥有的权限 服务实现类
 */
@Service
public class SysRolePermitServiceImpl extends ServiceImpl<SysRolePermitMapper, SysRolePermit> implements ISysRolePermitService {

}
