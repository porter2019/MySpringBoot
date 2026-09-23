package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户
 * </p>
 */
@Getter
@Setter
@ToString
@TableName("sys_user")
public class SysUser extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String cellPhone;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 岗位
     */
    private String jobName;

    /**
     * 密码
     */
    private String password;

    /**
     * 运营系统管理权限
     */
    @TableField("is_om")
    private Boolean isOM = true;

    /**
     * 移动端登录权限
     */
    @TableField("is_mp")
    private Boolean isMP = true;

    /**
     * 是否超管
     */
    private Boolean isSuper = false;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 微信公众号的OpenId
     */
    @TableField("wx_mp_open_id")
    private String wxMPOpenId;

    /**
     * 状态
     */
    private Boolean status = true;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
}
