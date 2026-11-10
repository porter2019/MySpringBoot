package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntityStandard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色
 */
@Getter
@Setter
@ToString
@TableName("sys_role")
public class SysRole extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

    /**
     * 组名
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否超级管理组
     */
    @TableField("is_super")
    private Boolean isSuper = false;

    /*
    create table sys_role
(
  id                bigint               auto_increment comment '主键自增id',
  name              varchar(30)                    null comment '岗位名称',
  sort              int              default 0 not null comment '排序数字',
  is_super          boolean      default false not null comment '是否超级管理组',
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
) comment '角色';
     */
}
