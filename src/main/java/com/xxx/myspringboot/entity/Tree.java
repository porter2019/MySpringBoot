package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 树形数据
 */
@Getter
@Setter
@ToString
public class Tree extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

    /**
     * 标识
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序数字
     */
    private String orderNo;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 父级名称
     */
    private String parentName;

    /**
     * 完整Id
     */
    private String fullId;

    /**
     * 完整名称
     */
    private String fullName;

    /**
     * 完整类别层级排序
     */
    private String fullOrderNo;

    /**
     * 层级
     */
    private Integer levelNo;

    /**
     * 状态
     */
    private Boolean status = true;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上级信息
     */
    @TableField(exist = false)
    @JsonIgnoreProperties({"parent", "childs"})
    private Tree parent;

    /**
     * 子级列表
     */
    @TableField(exist = false)
    @JsonManagedReference
    private List<Tree> childs;

    /*
      create table tree
      (
          id                bigint auto_increment comment '主键自增id',
          code              varchar(50)              null comment '标识',
          name              varchar(50)              null comment '名称',
          order_no          varchar(2000)            null comment '排序数字',
          parent_id         bigint                   null comment '父级id',
          parent_name       varchar(50)              null comment '父级名称',
          full_id           varchar(2000)            null comment '完整Id',
          full_name         varchar(2000)            null comment '完整名称',
          full_order_no     varchar(2000)            null comment '完整类别层级排序',
          level_no          int                      null comment '层级',
          status            tinyint(1) default 1     not null comment '状态',
          remark            varchar(200)             null comment '备注',
          created_user_id   long                     null comment '创建者用户Id',
          created_user_name varchar(30)              null comment '创建者用户名',
          created_time      datetime   default now() not null comment '创建时间',
          updated_user_id   long                     null comment '更新者用户Id',
          updated_user_name varchar(30)              null comment '更新者用户名',
          updated_time      datetime   default now() not null comment '更新时间',
          is_deleted        boolean    default false not null comment '软删除',
          constraint tree_pk primary key (id)
      )
          comment '树形数据';
     */

}
