package com.xxx.myspringboot.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLogoutParameter;
import cn.dev33.satoken.stp.parameter.enums.SaLogoutRange;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.common.ApiResult;
import com.xxx.myspringboot.common.CurrentConst;
import com.xxx.myspringboot.dto.input.LoginInput;
import com.xxx.myspringboot.entity.SysUser;
import com.xxx.myspringboot.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@SaCheckLogin
@RestController
@RequestMapping("/account")
@Tag(name = "账户")
public class AccountController {

    @Resource
    ISysUserService sysUserService;

    /**
     * 添加初始用户
     *
     * @return
     */
    @SaIgnore
    @GetMapping("init")
    @Operation(summary = "添加初始用户")
    public ApiResult InitAccount() {
        var user = new SysUser();
        user.setCellPhone("13000000000");
        user.setPassword(SecureUtil.md5("000"));
        user.setUserName("管理员");
        user.setIsSuper(true);
        sysUserService.saveOrUpdate(user);

        return ApiResult.success();
    }

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("login")
    @Operation(summary = "登录获取token")
    public ApiResult Login(@RequestBody LoginInput req, HttpServletResponse response) {
        var pwd = SecureUtil.md5(req.getPassword());
        var wrapper = new LambdaQueryWrapper<SysUser>();
        wrapper.eq(SysUser::getCellPhone, req.getCellPhone());
        wrapper.eq(SysUser::getPassword, pwd);
        var userEntity = sysUserService.getOne(wrapper);
        if (userEntity == null) {
            return ApiResult.error("用户不存在");
        }
        if (!userEntity.getStatus()) {
            return ApiResult.error("用户被禁用");
        }

        var updateWrapper = new LambdaUpdateWrapper<SysUser>();
        updateWrapper.eq(SysUser::getId, userEntity.getId()).set(SysUser::getLastLoginTime, LocalDateTime.now());
        sysUserService.update(updateWrapper);

        StpUtil.login(userEntity.getId());
        StpUtil.getSession().set(CurrentConst.UserId, userEntity.getId());
        StpUtil.getSession().set(CurrentConst.UserName, userEntity.getUserName());
        StpUtil.getSession().set(CurrentConst.CellPhone, userEntity.getCellPhone());
        StpUtil.getSession().set(CurrentConst.Is_Super_Admin, userEntity.getIsSuper());

        var tokenInfo = StpUtil.getTokenInfo();

        //将token输出到Knife4j中
        response.setHeader("access-token", tokenInfo.tokenValue);
        //Knife4jUI 的登录接口中，请求参数后面有一栏“AfterScript”，输入以下代码，自动将后续接口带上token
        //ke.global.setAllHeader(
        //  "Authorization",
        //  "Bearer " + ke.response.headers["access-token"]
        //);

        return ApiResult.success(tokenInfo.tokenValue);
    }

    @SaIgnore
    @GetMapping("islogin")
    @Operation(summary = "判断是否登录")
    public ApiResult IsLogin() {
        return ApiResult.success(StpUtil.isLogin() ? "！已登录！":"未登录");
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("logout")
    @Operation(summary = "退出登录")
    public ApiResult Logout() {
        // 所有这个账号的都注销
//        StpUtil.logout(StpUtil.getLoginId());
        // 当前客户端注销
        StpUtil.logout(new SaLogoutParameter()
                // 注销范围： TOKEN=只注销当前 token 的会话，ACCOUNT=注销当前 token 指向的 loginId 其所有客户端会话
                // 此参数只在调用 StpUtil.logout() 时有效
                .setRange(SaLogoutRange.TOKEN)
        );
        return ApiResult.success();
    }
}
