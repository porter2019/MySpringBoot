package com.xxx.myspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysRole.SysRolePageInput;
import com.xxx.myspringboot.entity.SysRole;
import com.xxx.myspringboot.mapper.SysRoleMapper;
import com.xxx.myspringboot.service.IPageQueryService;
import com.xxx.myspringboot.service.ISysRoleService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 用户组 服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService, IPageQueryService<SysRole> {

    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    public PageResult<SysRole> getPageList(SysRolePageInput input) {
        var wrapper = new LambdaQueryWrapper<SysRole>();
        wrapper.like(StringUtils.isNotBlank(input.getName()), SysRole::getName, input.getName());

        return myPageQuery(wrapper, input.getPageInfo());
    }


}
