package com.xxx.myspringboot.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.common.CurrentConst;
import com.xxx.myspringboot.dto.input.SysUser.SysUserChangeAvatarInput;
import com.xxx.myspringboot.dto.input.SysUser.SysUserChangePwdInput;
import com.xxx.myspringboot.entity.SysUser;
import com.xxx.myspringboot.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 上下文控制器
 */
@Slf4j
@SaCheckLogin
@RestController
@Tag(name = "上下文")
@RequestMapping("/current")
public class CurrentController {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private Environment env;

    @PostMapping("get/user/info")
    @Operation(summary = "获取当前登录的用户信息")
    public ApiResult GetUserInfo() {
        var userId = StpUtil.getLoginIdAsLong();
        var userInfo = sysUserService.getById(userId);
        if (userInfo == null) {
            return ApiResult.failed("用户不存在");
        }
        if (!userInfo.getStatus()) {
            return ApiResult.failed("用户已被禁用");
        }

        var avatarUrl = userInfo.getAvatar();
        if (StringUtils.isNotBlank(avatarUrl)) {
            avatarUrl = env.getProperty("app.domain-url") + avatarUrl;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("UserId", userInfo.getId());
        result.put("CellPhone", userInfo.getCellPhone());
        result.put("LoginName", userInfo.getLoginName());
//        result.put("UserName", userInfo.getUserName());
        result.put("UserName", StpUtil.getSession().get(CurrentConst.UserName));
        result.put("Avatar", avatarUrl);
        result.put("LastLoginTime", userInfo.getLastLoginTime());
        result.put("IsSuper", userInfo.getIsSuper());
        return ApiResult.success(result);
    }

    @GetMapping("modify/username")
    @Operation(summary = "修改用户名")
    public ApiResult ChangeUserName(@RequestParam String userName) {
        if(StringUtils.isBlank(userName)){
            return ApiResult.failed("名称不能为空");
        }
        var userInfo = sysUserService.getById(StpUtil.getLoginIdAsLong());
        if (userInfo == null) {
            return ApiResult.failed("用户不存在");
        }
        if (!userInfo.getStatus()) {
            return ApiResult.failed("用户已被禁用");
        }
        //指定更新
        var updateWrapper = new LambdaUpdateWrapper<SysUser>();
        updateWrapper.eq(SysUser::getId, userInfo.getId())
                .set(SysUser::getUserName, userName)
                .set(SysUser::getUpdatedUserId, userInfo.getId())
                .set(SysUser::getUpdatedUserName, userInfo.getUserName())
                .set(SysUser::getUpdatedTime, LocalDateTime.now());
        sysUserService.update(updateWrapper);

        // 更新上下文中的用户名
        StpUtil.getSession().set(CurrentConst.UserName, userName);

        return ApiResult.success("修改成功");
    }

    @PostMapping("modify/avatar")
    @Operation(summary = "修改头像")
    public  ApiResult ChangeAvatar(@RequestBody @Valid SysUserChangeAvatarInput req) {
        var userInfo = sysUserService.getById(StpUtil.getLoginIdAsLong());
        if (userInfo == null) {
            return ApiResult.failed("用户不存在");
        }
        if (!userInfo.getStatus()) {
            return ApiResult.failed("用户已被禁用");
        }
        //指定更新
        var updateWrapper = new LambdaUpdateWrapper<SysUser>();
        updateWrapper.eq(SysUser::getId, userInfo.getId())
                .set(SysUser::getAvatar, req.getAvatar())
                .set(SysUser::getUpdatedUserId, userInfo.getId())
                .set(SysUser::getUpdatedUserName, userInfo.getUserName())
                .set(SysUser::getUpdatedTime, LocalDateTime.now());
        sysUserService.update(updateWrapper);

        return ApiResult.success("修改成功");
    }

    @PostMapping("modify/password")
    @Operation(summary = "修改密码")
    public ApiResult ChangePassword(@Valid @RequestBody SysUserChangePwdInput req) {
        var userInfo = sysUserService.getById(StpUtil.getLoginIdAsLong());
        if (userInfo == null) {
            return ApiResult.failed("用户不存在");
        }
        if (!userInfo.getStatus()) {
            return ApiResult.failed("用户已被禁用");
        }

        var oldPwd = SecureUtil.md5(req.getOldPassword());
        var newPwd = SecureUtil.md5(req.getNewPassword());
        if (!oldPwd.equals(userInfo.getPassword())) {
            return ApiResult.failed("原密码错误");
        }

        //指定更新
        var updateWrapper = new LambdaUpdateWrapper<SysUser>();
        updateWrapper.eq(SysUser::getId, userInfo.getId())
                .set(SysUser::getPassword, newPwd)
                .set(SysUser::getUpdatedUserId, userInfo.getId())
                .set(SysUser::getUpdatedUserName, userInfo.getUserName())
                .set(SysUser::getUpdatedTime, LocalDateTime.now());
        sysUserService.update(updateWrapper);

        return ApiResult.success("修改成功");
//这样每个字段都更新了
//        userInfo.setPassword(newPwd);
//        sysUserService.updateById(userInfo);
    }

}
