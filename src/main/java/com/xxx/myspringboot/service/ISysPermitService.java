package com.xxx.myspringboot.service;

import com.xxx.myspringboot.entity.SysPermit;
import com.baomidou.mybatisplus.spring.service.IService;

import java.util.List;

/**
 * 功能下的权限 服务类
 */
public interface ISysPermitService extends IService<SysPermit> {

    /**
     * 根据角色 ids 获取所拥有的权限码
     *
     * @param roleIds 角色 ids
     * @return 权限码列表
     */
    List<String> getPermissionCodesByRoleIds(List<Long> roleIds);

    /**
     * 根据用户 id 获取所拥有的权限码
     *
     * @param userId 用户 id
     * @return 权限码列表
     */
    List<String> getPermissionCodesByUserId(Long userId);

    /**
     * 查询拥有指定权限的用户Id列表
     *
     * @param handlerName 功能别名，如 SysUser
     * @param actionName  权限别名，如 show
     */
    List<Long> getUserIdByPermission(String handlerName, String actionName);
}
