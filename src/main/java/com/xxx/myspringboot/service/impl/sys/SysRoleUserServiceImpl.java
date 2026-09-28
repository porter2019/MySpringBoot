package com.xxx.myspringboot.service.impl.sys;

import com.xxx.myspringboot.entity.sys.SysRoleUser;
import com.xxx.myspringboot.mapper.sys.SysRoleUserMapper;
import com.xxx.myspringboot.service.sys.ISysRoleUserService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户组下的用户 服务实现类
 */
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements ISysRoleUserService {

}
