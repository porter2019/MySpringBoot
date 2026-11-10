package com.xxx.myspringboot.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 标准实体需要继承的，包括创建人、修改人等信息
 */
@Getter
@Setter
@ToString
public class BaseEntityStandard extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 创建者用户Id
     */
    @TableField(value = "created_user_id", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Long createdUserId;

    /**
     * 创建者用户名
     */
    @TableField(value = "created_user_name", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private String createdUserName;

    /**
     * 修改者用户Id
     */
    @TableField(value = "updated_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updatedUserId;

    /**
     * 修改者用户名
     */
    @TableField(value = "updated_user_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedUserName;

    /**
     * 状态
     */
    private Boolean status = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getStatusText() {
        return status ? "启用" : "禁用";
    }

    /**
     * 备注
     */
    private String remark;

    /* 建表模板
      create table 表名
(
  id                bigint               auto_increment comment '主键自增id',

  `code`            varchar(50)                    null comment '岗位编码',
  name              varchar(30)                    null comment '岗位名称',

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
) comment '表说明';
     */
}
