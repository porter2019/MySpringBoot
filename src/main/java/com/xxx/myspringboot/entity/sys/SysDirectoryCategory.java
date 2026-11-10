package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xxx.myspringboot.entity.base.BaseEntityTree;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 字典类别
 */
@Getter
@Setter
@ToString
@TableName("sys_directory_category")
public class SysDirectoryCategory extends BaseEntityTree {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;


    /**
     * 上级信息
     */
    @TableField(exist = false)
    @JsonIgnoreProperties({"parent", "childs"})
    private SysOrg parent;

    /**
     * 子级列表
     */
    @TableField(exist = false)
    @JsonManagedReference
    private List<SysOrg> childs;

    /*
    create table sys_directory_category
        (
          id                bigint                auto_increment comment '主键自增id',
          name              varchar(50)                     null comment '名称',
          code       varchar(30)                     null comment '负责人名称',
          order_no              varchar(2000)                   null comment '排序数字',
          parent_id         bigint                          null comment '父级id',
          parent_name       varchar(50)                     null comment '父级名称',
          full_id           varchar(2000)                   null comment '完整Id',
          full_name         varchar(2000)                   null comment '完整名称',
          full_order_no         varchar(2000)                   null comment '完整类别层级排序',
          level_no          int                             null comment '层级',
          status            tinyint(1)        default 1 not null comment '状态',
          remark            varchar(200)                    null comment '备注',
          created_user_id   bigint                          null comment '创建者用户Id',
          created_user_name varchar(30)                     null comment '创建者用户名',
          created_time      datetime      default now() not null comment '创建时间',
          updated_user_id   bigint                          null comment '更新者用户Id',
          updated_user_name varchar(30)                     null comment '更新者用户名',
          updated_time      datetime      default now() not null comment '更新时间',
          is_deleted        boolean       default false not null comment '软删除',
          primary key (id)
        ) comment '字典类别';
     */
}
