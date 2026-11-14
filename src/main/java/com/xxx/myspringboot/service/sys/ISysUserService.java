package com.xxx.myspringboot.service.sys;

import com.xxx.myspringboot.entity.sys.SysUser;
import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.entity.sys.SysUserOMView;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 系统用户
 */
public interface ISysUserService extends IService<SysUser> {
    long add(SysUserOMView entity);

    boolean edit(SysUserOMView entity);

    boolean modifyUserName(Long userId, String userName);

    boolean delete(Set<Long> idList);
}
