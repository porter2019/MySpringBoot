package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

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
}
