package com.xxx.myspringboot.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.common.PublicConst;
import com.xxx.myspringboot.entity.SysRoleUser;
import com.xxx.myspringboot.entity.SysUser;
import com.xxx.myspringboot.entity.SysUserOMView;
import com.xxx.myspringboot.mapper.SysUserMapper;
import com.xxx.myspringboot.service.ISysRoleUserService;
import com.xxx.myspringboot.service.ISysUserService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户 服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private ISysRoleUserService sysRoleUserService;

    // 添加用户及所属的用户组
    @Transactional(rollbackFor = Exception.class)
    public long Add(SysUserOMView entity) {
        entity.setIsSuper(false);
        entity.setIsOM(false);
        entity.setIsMP(true);
        entity.setPassword(SecureUtil.md5(entity.getPassword()));
        save(entity);

        //用户组
        List<SysRoleUser> userRoleList = new ArrayList<>();
        for (long x : entity.getRoleIdArray()) {
            var role = new SysRoleUser();
            role.setUserId(entity.getId());
            role.setRoleId(x);
            userRoleList.add(role);
        }
        sysRoleUserService.saveBatch(userRoleList);


        return entity.getId();
    }

    // 修改用户及所属的用户组
    @Transactional(rollbackFor = Exception.class)
    public boolean Edit(SysUserOMView entity) {
        var oldEntity = getById(entity.getId());
        if (!entity.getPassword().equals(PublicConst.Display_User_Password)) {
            //修改了密码
            entity.setPassword(SecureUtil.md5(entity.getPassword()));
        } else {
            entity.setPassword(oldEntity.getPassword());
        }
        entity.setIsSuper(oldEntity.getIsSuper());
        updateById(entity);

        //先删除原来的组信息
        var deleteWrapper = new LambdaQueryWrapper<SysRoleUser>();
        deleteWrapper.eq(SysRoleUser::getUserId, entity.getId());
        sysRoleUserService.remove(deleteWrapper);
        //添加新的用户组
        List<SysRoleUser> userRoleList = new ArrayList<>();
        for (long x : entity.getRoleIdArray()) {
            var role = new SysRoleUser();
            role.setUserId(entity.getId());
            role.setRoleId(x);
            userRoleList.add(role);
        }
        sysRoleUserService.saveBatch(userRoleList);
        return true;
    }

    // 删除
    @Transactional
    public boolean Delete(List<Long> idList) {
        if (idList.isEmpty()) {
            return false;
        }

        // 删除用户组
        var roleUserLambdaQueryWrapper = new LambdaQueryWrapper<SysRoleUser>();
        roleUserLambdaQueryWrapper.in(SysRoleUser::getUserId, idList);
        sysRoleUserService.remove(roleUserLambdaQueryWrapper);

        //循环修改手机号，保证手机号都是唯一的
        long timestamp = System.currentTimeMillis();
        var userUpdateWrapper = new LambdaUpdateWrapper<SysUser>();
        userUpdateWrapper.in(SysUser::getId, idList).setSql("cell_phone = CONCAT(cell_phone, '-', " + timestamp + ") ");
        var isUpdate = update(userUpdateWrapper);
        //再进行逻辑删除
        var userDeleteWrapper = new LambdaQueryWrapper<SysUser>();
        userDeleteWrapper.in(SysUser::getId, idList);
        remove(userDeleteWrapper);

        return isUpdate;
    }
}
