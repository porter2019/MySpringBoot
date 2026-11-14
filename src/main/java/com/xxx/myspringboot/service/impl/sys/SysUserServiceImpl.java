package com.xxx.myspringboot.service.impl.sys;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.common.CurrentConst;
import com.xxx.myspringboot.common.PublicConst;
import com.xxx.myspringboot.entity.sys.*;
import com.xxx.myspringboot.mapper.sys.SysOrgMapper;
import com.xxx.myspringboot.mapper.sys.SysUserMapper;
import com.xxx.myspringboot.service.sys.ISysPostUserService;
import com.xxx.myspringboot.service.sys.ISysRoleUserService;
import com.xxx.myspringboot.service.sys.ISysUserService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 系统用户 服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final ISysRoleUserService sysRoleUserService;
    private final ISysPostUserService sysPostUserService;
    private final SysOrgMapper sysOrgMapper;

    // 添加用户及所属的角色和岗位
    @Transactional(rollbackFor = Exception.class)
    public long add(SysUserOMView entity) {
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

        //岗位
        List<SysPostUser> userPostList = new ArrayList<>();
        for (long x : entity.getPostIdArray()) {
            var post = new SysPostUser();
            post.setUserId(entity.getId());
            post.setPostId(x);
            userPostList.add(post);
        }
        sysPostUserService.saveBatch(userPostList);


        return entity.getId();
    }

    // 修改用户及所属的用户组
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(SysUserOMView entity) {
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

        //先删除原来的岗位信息
        var deletePostWrapper = new LambdaQueryWrapper<SysPostUser>();
        deletePostWrapper.eq(SysPostUser::getUserId, entity.getId());
        sysPostUserService.remove(deletePostWrapper);
        //添加新的岗位
        List<SysPostUser> userPostList = new ArrayList<>();
        for (long x : entity.getPostIdArray()) {
            var post = new SysPostUser();
            post.setUserId(entity.getId());
            post.setPostId(x);
            userPostList.add(post);
        }
        sysPostUserService.saveBatch(userPostList);

        //部门中的领导，冗余存储的
        var updateOrgWrapper = new LambdaUpdateWrapper<SysOrg>();
        updateOrgWrapper.eq(SysOrg::getLeaderId, entity.getId()).set(SysOrg::getLeaderName, entity.getUserName());
        sysOrgMapper.update(updateOrgWrapper);

        return true;
    }

    // 修改用户名
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyUserName(Long userId, String userName) {
        var model = getById(userId);
        if (model == null) {
            return false;
        }
        if (!model.getStatus()) {
            return false;
        }

        var updateWrapper = new LambdaUpdateWrapper<SysUser>();
        updateWrapper.eq(SysUser::getId, userId)
                .set(SysUser::getUserName, userName)
                .set(SysUser::getUpdatedUserId, StpUtil.getLoginIdAsLong())
                .set(SysUser::getUpdatedUserName, StpUtil.getSession().get(CurrentConst.UserName).toString())
                .set(SysUser::getUpdatedTime, LocalDateTime.now());
        update(updateWrapper);

        //部门中的领导，冗余存储的
        var updateOrgWrapper = new LambdaUpdateWrapper<SysOrg>();
        updateOrgWrapper.eq(SysOrg::getLeaderId, userId).set(SysOrg::getLeaderName, userName);
        sysOrgMapper.update(updateOrgWrapper);

        return true;
    }

    // 删除
    @Transactional
    public boolean delete(Set<Long> idList) {
        if (idList.isEmpty()) {
            return false;
        }

        // 删除用户组
        var roleUserLambdaQueryWrapper = new LambdaQueryWrapper<SysRoleUser>();
        roleUserLambdaQueryWrapper.in(SysRoleUser::getUserId, idList);
        sysRoleUserService.remove(roleUserLambdaQueryWrapper);

        //删除岗位
        var postUserLambdaQueryWrapper = new LambdaQueryWrapper<SysPostUser>();
        postUserLambdaQueryWrapper.in(SysPostUser::getUserId, idList);
        sysPostUserService.remove(postUserLambdaQueryWrapper);

        //循环修改手机号，保证手机号都是唯一的
        long timestamp = System.currentTimeMillis();
        var userUpdateWrapper = new LambdaUpdateWrapper<SysUser>();
        userUpdateWrapper.in(SysUser::getId, idList).setSql("cell_phone = CONCAT(cell_phone, '-', " + timestamp + ") ");
        var isUpdate = update(userUpdateWrapper);
        //再进行逻辑删除
        var userDeleteWrapper = new LambdaQueryWrapper<SysUser>();
        userDeleteWrapper.in(SysUser::getId, idList);
        remove(userDeleteWrapper);

        //部门中的领导，冗余存储的
        var updateOrgWrapper = new LambdaUpdateWrapper<SysOrg>();
        updateOrgWrapper.in(SysOrg::getLeaderId, idList).set(SysOrg::getLeaderId, 0).set(SysOrg::getLeaderName, null);
        sysOrgMapper.update(updateOrgWrapper);

        return isUpdate;
    }
}
