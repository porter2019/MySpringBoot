package com.xxx.myspringboot.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.xxx.myspringboot.common.CurrentConst;
import com.xxx.myspringboot.service.ISysPermitService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 获取用户权限实现类
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private ISysPermitService sysPermitService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        var userId = StpUtil.getLoginIdAsLong();
        var isSuper = Boolean.parseBoolean(StpUtil.getSession().getString(CurrentConst.Is_Super_Admin));
        if (isSuper) {
            log.info("超级管理员拥有所有权限,返回权限：*");
            return Collections.singletonList("*");
        }
        return sysPermitService.getPermissionCodesByUserId(userId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return Collections.emptyList();
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
//        List<String> list = new ArrayList<String>();
//        list.add("admin");
//        list.add("super-admin");
//        return list;
    }
}
