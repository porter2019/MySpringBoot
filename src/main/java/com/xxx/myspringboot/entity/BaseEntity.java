package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基本实体类
 */
@Getter
@Setter
@ToString
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    /**
     * 是否软删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField("is_deleted")
    @JsonIgnore
    private Boolean isDeleted;

    /** 建表模板
     * create table 表名
     * (
     *     id                bigint auto_increment comment '主键自增id',
     *
     *     cellPhone         varchar(100)             null comment '手机号',
     *
     *     created_time      datetime   default now() not null comment '创建时间',
     *     updated_time      datetime   default now() not null comment '更新时间',
     *     is_deleted        boolean    default false not null comment '软删除',
     *     constraint 表名_pk  primary key (id)
     * )
     *     comment '表说明';
     */
}

