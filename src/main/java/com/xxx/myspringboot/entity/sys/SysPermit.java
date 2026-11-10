package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 功能下的权限
 */
@Getter
@Setter
@ToString
@TableName("sys_permit")
public class SysPermit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属功能Id
     */
    private Long handlerId;

    /**
     * 模块名称
     */
    private String permitName;

    /**
     * 功能别名
     */
    private String aliasName;

    /*
    create table sys_permit
(
  id           bigint              auto_increment comment '主键自增id',
  handler_id   bigint                    not null comment '所属功能Id',
  permit_name  varchar(50)                   null comment '模块名称',
  alias_name   varchar(50)                   null comment '功能别名',
  created_time datetime    default now() not null comment '创建时间',
  updated_time datetime    default now() not null comment '更新时间',
  is_deleted   boolean     default false not null comment '软删除',
  primary key (id)
) comment '功能下的权限';
     */
}
