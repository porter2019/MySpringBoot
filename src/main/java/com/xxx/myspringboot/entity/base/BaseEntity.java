package com.xxx.myspringboot.entity.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.fesod.sheet.annotation.ExcelIgnore;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.format.DateTimeFormat;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

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
    @ExcelProperty("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(18)
    @TableField(value = "created_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @ExcelIgnore
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    /**
     * 是否软删除
     */
    @ExcelIgnore
    @TableLogic(value = "0", delval = "1")
    @TableField("is_deleted")
    @JsonIgnore
    private Boolean isDeleted;

    /* 建表模板
      create table 表名
      (
          id                bigint auto_increment comment '主键自增id',

          cellPhone         varchar(100)             null comment '手机号',

          created_time      datetime   default now() not null comment '创建时间',
          updated_time      datetime   default now() not null comment '更新时间',
          is_deleted        boolean    default false not null comment '软删除',
          primary key (id)
      )
          comment '表说明';
     */
}

