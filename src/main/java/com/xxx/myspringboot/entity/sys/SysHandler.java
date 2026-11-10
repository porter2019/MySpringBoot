package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统模块下的功能
 */
@Getter
@Setter
@ToString
@TableName("sys_handler")
public class SysHandler extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属模块Id
     */
    private Long moduleId;

    /**
     * 功能名称
     */
    private String handlerName;

    /**
     * 功能别名
     */
    private String aliasName;

    /**
     * 关联控制器
     */
    private String refController;

    /**
     * 排序数字
     */
    private Integer sort;

    /*
    create table sys_handler
(
  id             bigint               auto_increment comment '主键自增id',
  module_id      bigint                     not null comment '所属模块Id',
  handler_name   varchar(50)                    null comment '功能名称',
  alias_name     varchar(50)                    null comment '功能别名',
  ref_controller varchar(500)                   null comment '关联控制器',
  sort           int              default 0 not null comment '排序数字',
  created_time   datetime     default now() not null comment '创建时间',
  updated_time   datetime     default now() not null comment '更新时间',
  is_deleted     boolean      default false not null comment '软删除',
  primary key (id)
) comment '模块下的功能';
     */
}
