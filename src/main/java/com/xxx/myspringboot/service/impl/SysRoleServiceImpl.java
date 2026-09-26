package com.xxx.myspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysRole.SysRolePageInput;
import com.xxx.myspringboot.dto.output.permit.SysRoleHandlerGroupOutput;
import com.xxx.myspringboot.dto.output.permit.SysRoleModuleGroupOutput;
import com.xxx.myspringboot.dto.output.permit.SysRolePermitOutput;
import com.xxx.myspringboot.entity.SysRole;
import com.xxx.myspringboot.entity.SysRolePermit;
import com.xxx.myspringboot.mapper.SysPermitMapper;
import com.xxx.myspringboot.mapper.SysRoleMapper;
import com.xxx.myspringboot.mapper.SysRolePermitMapper;
import com.xxx.myspringboot.service.IPageQueryService;
import com.xxx.myspringboot.service.ISysRoleService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户组 服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService, IPageQueryService<SysRole> {

    private final SysPermitMapper sysPermitMapper;
    private final SysRolePermitMapper sysRolePermitMapper;
    private final SysRoleMapper sysRoleMapper;

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

    /**
     * 获取某角色下所有权限及其勾选状态
     *
     * @param roleId 角色id
     * @return 权限列表
     */
    public List<SysRoleModuleGroupOutput> getPermitListByRoleId(Long roleId) {

        List<SysRolePermitOutput> dbData = sysPermitMapper.selectRolePermitList(roleId);

        // 按 moduleName 分组
        Map<String, List<SysRolePermitOutput>> moduleGroup = dbData.stream()
                .collect(Collectors.groupingBy(SysRolePermitOutput::getModuleName, LinkedHashMap::new, Collectors.toList()));

        List<SysRoleModuleGroupOutput> resultList = new ArrayList<>();
        for (Map.Entry<String, List<SysRolePermitOutput>> moduleEntry : moduleGroup.entrySet()) {
            SysRoleModuleGroupOutput moduleModel = new SysRoleModuleGroupOutput(moduleEntry.getKey());

            Map<String, List<SysRolePermitOutput>> handlerGroup = moduleEntry.getValue().stream()
                    .collect(Collectors.groupingBy(SysRolePermitOutput::getHandlerName, LinkedHashMap::new, Collectors.toList()));

            for (Map.Entry<String, List<SysRolePermitOutput>> handlerEntry : handlerGroup.entrySet()) {
                SysRoleHandlerGroupOutput handlerModel = new SysRoleHandlerGroupOutput(handlerEntry.getKey());
                handlerEntry.getValue().sort(Comparator.comparingInt(SysRolePermitOutput::getOrderNo).reversed());
                handlerModel.setPermitList(handlerEntry.getValue());
                moduleModel.getHandlerList().add(handlerModel);
            }
            resultList.add(moduleModel);
        }
        return resultList;
    }

    /**
     * 设置角色权限
     *
     * @param roleId  角色id
     * @param permits 权限id列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setRolePermit(Long roleId, String permits) {
        sysRolePermitMapper.delete(new LambdaQueryWrapper<SysRolePermit>()
                .eq(SysRolePermit::getRoleId, roleId));

        if (StringUtils.isNotBlank(permits)) {
            List<SysRolePermit> rolePermitList = new ArrayList<>();
            for (String item : permits.split(",")) {
                String trimmed = item.trim();
                if (StringUtils.isBlank(trimmed)) continue;
                SysRolePermit rp = new SysRolePermit();
                rp.setRoleId(roleId);
                rp.setPermitId(Long.valueOf(trimmed));
                rolePermitList.add(rp);
            }
            if (!rolePermitList.isEmpty()) {
                sysRolePermitMapper.insert(rolePermitList);
            }
        }
        return true;
    }


    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID，用于查询该用户对应的角色信息
     * @return 返回用户对应的角色列表，如果用户没有角色则返回空列表
     */
    public List<SysRole> getRoleListByUserId(Long userId) {
        return sysRoleMapper.selectRoleListByUserId(userId);
    }

}
