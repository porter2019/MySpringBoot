package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户组
 */
@Getter
@Setter
@ToString
@TableName("sys_role")
public class SysRole extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;


    /**
     * 组名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否超级管理组
     */
    @TableField("is_super")
    private Boolean isSuper = false;

    /**
     * 状态
     */
    private Boolean status = true;
}
