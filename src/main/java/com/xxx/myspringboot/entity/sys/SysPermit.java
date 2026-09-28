package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
