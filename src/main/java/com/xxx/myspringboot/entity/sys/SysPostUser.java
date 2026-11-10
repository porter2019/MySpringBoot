package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 岗位下的用户
 */
@Getter
@Setter
@ToString
@TableName("sys_post_user")
public class SysPostUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位Id
     */
    private Long postId;

    /**
     * 用户Id
     */
    private Long userId;

    /*
    create table sys_post_user
(
  id           bigint           auto_increment comment '主键自增id',
  post_id      bigint                 not null comment '岗位Id',
  user_id      bigint                 not null comment '用户Id',
  created_time datetime default now() not null comment '创建时间',
  updated_time datetime default now() not null comment '更新时间',
  is_deleted   boolean  default false not null comment '软删除',
  primary key (id)
) comment '岗位下的用户';
     */
}
