package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色所拥有的权限
 */
@Getter
@Setter
@ToString
@TableName("sys_role_permit")
public class SysRolePermit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户组Id
     */
    private Long roleId;

    /**
     * 权限Id
     */
    private Long permitId;

    /*
    create table sys_role_permit
(
  id           bigint           auto_increment comment '主键自增id',
  role_id      bigint                 not null comment '角色Id',
  permit_id    bigint                 not null comment '权限Id',
  created_time datetime default now() not null comment '创建时间',
  updated_time datetime default now() not null comment '更新时间',
  is_deleted   boolean  default false not null comment '软删除',
  primary key (id)
) comment '角色所拥有的权限';
     */
}
