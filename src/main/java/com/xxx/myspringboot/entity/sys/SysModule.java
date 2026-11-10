package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统模块
 */
@Getter
@Setter
@ToString
@TableName("sys_module")
public class SysModule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 排序数字
     */
    private Integer sort;

    /*
    create table sys_module
(
  id             bigint               auto_increment comment '主键自增id',
  module_name   varchar(50)                    null comment '模块名称',
  sort           int              default 0 not null comment '排序数字',
  created_time   datetime     default now() not null comment '创建时间',
  updated_time   datetime     default now() not null comment '更新时间',
  is_deleted     boolean      default false not null comment '软删除',
  primary key (id)
) comment '系统模块';
     */
}
