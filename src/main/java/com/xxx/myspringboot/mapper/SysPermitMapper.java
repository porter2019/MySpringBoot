package com.xxx.myspringboot.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.myspringboot.dto.output.permit.SysRolePermitOutput;
import com.xxx.myspringboot.entity.SysPermit;

import java.util.List;

/**
 * 功能下的权限 Mapper 接口
 */
public interface SysPermitMapper extends BaseMapper<SysPermit> {

    /**
     * 查询某角色下所有权限及其勾选状态
     * 方法名跟xml中 select id="selectRolePermitList"保持一致
     *
     * @param roleId 角色id
     */
    List<SysRolePermitOutput> selectRolePermitList(@Param("roleId") Long roleId);

    /**
     * 根据角色 ids 获取所拥有的权限码
     *
     * @param roleIds 角色 ids
     * @return 权限码列表
     */
    List<String> selectPermissionCodesByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据用户 id 获取所拥有的权限码
     *
     * @param userId 用户 id
     * @return 权限码列表
     */
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);

    /**
     * 查询拥有指定权限的用户Id列表
     * @param handlerAliasName 功能别名，如 SysUser
     * @param actionAliasName  权限别名，如 show
     */
    List<Long> selectUserIdsByPermission(@Param("handlerAliasName") String handlerAliasName,
                                         @Param("actionAliasName") String actionAliasName);
}
