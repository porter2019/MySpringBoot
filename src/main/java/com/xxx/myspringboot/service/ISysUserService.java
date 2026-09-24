package com.xxx.myspringboot.service;

import com.xxx.myspringboot.entity.SysUser;
import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.entity.SysUserOMView;

import java.util.List;

/**
 * 系统用户
 */
public interface ISysUserService extends IService<SysUser> {
    long Add(SysUserOMView entity);

    boolean Edit(SysUserOMView entity);

    boolean Delete(List<Long> idList);
}
