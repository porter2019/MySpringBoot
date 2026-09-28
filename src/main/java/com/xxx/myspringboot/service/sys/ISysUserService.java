package com.xxx.myspringboot.service.sys;

import com.xxx.myspringboot.entity.sys.SysUser;
import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.entity.sys.SysUserOMView;

import java.util.List;

/**
 * 系统用户
 */
public interface ISysUserService extends IService<SysUser> {
    long Add(SysUserOMView entity);

    boolean Edit(SysUserOMView entity);

    boolean Delete(List<Long> idList);
}
