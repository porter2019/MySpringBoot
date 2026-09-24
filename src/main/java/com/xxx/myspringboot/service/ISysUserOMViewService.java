package com.xxx.myspringboot.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysUser.SysUserPageInput;
import com.xxx.myspringboot.entity.SysUserOMView;

/**
 * 系统用户运营视图 服务实现类
 */
public interface ISysUserOMViewService  extends IService<SysUserOMView> {

    /**
     * 获取分页列表
     * @param input input
     * @return 分页对象
     */
    PageResult<SysUserOMView> getPageList(SysUserPageInput input);
}
