package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntityStandard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 系统用户
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
     * 性别
     */
    private String gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 部门Id
     */
    private Long orgId;

    /**
     * 部门名称
     */
    private String orgName; //冗余存储

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
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /*
     create table sys_user
     (
     id                bigint               auto_increment comment '主键自增id',
     cell_phone        varchar(20)                    null comment '手机号',
     login_name        varchar(30)                    null comment '登陆名',
     user_name         varchar(30)                    null comment '用户名',
     gender            VARCHAR(20)                    null COMMENT '性别',
     email             varchar(50)                    null comment '邮箱',
     `password`        varchar(500)                   null comment '密码',
     org_id            bigint                         null comment '部门Id',
     org_name          varchar(50)                    null comment '部门名称',
     is_om             boolean       default true not null comment '运营系统管理权限',
     is_mp             boolean       default true not null comment '移动端登录权限',
     is_super         boolean      default false not null comment '是否超管',
     avatar            varchar(500)                   null comment '头像',
     wx_mp_open_id     varchar(50)                    null comment '微信公众号的OpenId',
     last_login_time   datetime                       null comment '最后登陆时间',
     last_login_ip     varchar(50)                    null comment '最后登录IP',
     `status`          tinyint(1)       default 1 not null comment '状态',
     remark            varchar(200)                   null comment '备注',
     created_user_id   bigint                         null comment '创建者用户Id',
     created_user_name varchar(30)                    null comment '创建者用户名',
     created_time      datetime     default now() not null comment '创建时间',
     updated_user_id   bigint                         null comment '更新者用户Id',
     updated_user_name varchar(30)                    null comment '更新者用户名',
     updated_time      datetime     default now() not null comment '更新时间',
     is_deleted        boolean      default false not null comment '软删除',
     primary key (id)
     ) comment '系统用户';

     */
}
