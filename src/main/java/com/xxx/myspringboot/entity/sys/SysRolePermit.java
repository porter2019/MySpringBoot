package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户组所拥有的权限
 */
@Getter
@Setter
@ToString
@TableName("sys_role_permit")
public class SysRolePermit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户组Id
     */
    private Long roleId;

    /**
     * 权限Id
     */
    private Long permitId;
}
