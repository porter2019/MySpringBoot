package com.xxx.myspringboot.service;

import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysRole.SysRolePageInput;
import com.xxx.myspringboot.entity.SysRole;
import com.baomidou.mybatisplus.spring.service.IService;

/**
 * 用户组 服务类
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    PageResult<SysRole> getPageList(SysRolePageInput input);
}
