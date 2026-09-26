package com.xxx.myspringboot.service;

import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysRole.SysRolePageInput;
import com.xxx.myspringboot.dto.output.permit.SysRoleModuleGroupOutput;
import com.xxx.myspringboot.entity.SysRole;
import com.baomidou.mybatisplus.spring.service.IService;

import java.util.List;

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

    /**
     * 获取某角色下所有权限及其勾选状态
     *
     * @param roleId 角色id
     * @return 权限列表
     */
    List<SysRoleModuleGroupOutput> getPermitListByRoleId(Long roleId);

    /**
     * 设置角色权限
     *
     * @param roleId  角色id
     * @param permits 权限id列表
     * @return 是否成功
     */
    boolean setRolePermit(Long roleId, String permits);

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID，用于查询该用户所属的角色信息
     * @return 返回该用户所拥有的所有角色列表，类型为List<SysRole>
     */
    List<SysRole> getRoleListByUserId(Long userId);
}
