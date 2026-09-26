package com.xxx.myspringboot.service.impl;

import com.xxx.myspringboot.entity.SysPermit;
import com.xxx.myspringboot.mapper.SysPermitMapper;
import com.xxx.myspringboot.service.ISysPermitService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 功能下的权限 服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysPermitServiceImpl extends ServiceImpl<SysPermitMapper, SysPermit> implements ISysPermitService {
    private final SysPermitMapper sysPermitMapper;

    /**
     * 根据角色 ids 获取所拥有的权限码
     *
     * @param roleIds 角色 ids
     * @return 权限码列表
     */
    public List<String> getPermissionCodesByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return sysPermitMapper.selectPermissionCodesByRoleIds(roleIds);
    }

    /**
     * 根据用户 id 获取所拥有的权限码
     *
     * @param userId 用户 id
     * @return 权限码列表
     */
    public List<String> getPermissionCodesByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return sysPermitMapper.selectPermissionCodesByUserId(userId);
    }

    /**
     * 查询拥有指定权限的用户Id列表
     *
     * @param handlerName 功能别名，如 SysUser
     * @param actionName  权限别名，如 show
     */
    public List<Long> getUserIdByPermission(String handlerName, String actionName) {
        if (StringUtils.isBlank(handlerName)) return Collections.emptyList();
        if (StringUtils.isBlank(actionName)) actionName = "show";
        return sysPermitMapper.selectUserIdsByPermission(handlerName, actionName);
    }

}
