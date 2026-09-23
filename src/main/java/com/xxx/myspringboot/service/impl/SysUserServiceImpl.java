package com.xxx.myspringboot.service.impl;

import com.xxx.myspringboot.entity.SysUser;
import com.xxx.myspringboot.mapper.SysUserMapper;
import com.xxx.myspringboot.service.ISysUserService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author X
 * @since 2026-09-22
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
