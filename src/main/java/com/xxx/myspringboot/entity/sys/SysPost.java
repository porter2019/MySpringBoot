package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntityStandard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 岗位
 */
@Getter
@Setter
@ToString
@TableName("sys_post")
public class SysPost extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序数字
     */
    private Integer sort;

    /*

    create table sys_post
(
  id                bigint               auto_increment comment '主键自增id',
  `code`            varchar(50)                    null comment '岗位编码',
  name              varchar(30)                    null comment '岗位名称',
  sort              int              default 0 not null comment '排序数字',
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
) comment '岗位';

     */

}
