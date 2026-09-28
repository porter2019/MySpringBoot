package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntityStandard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户组下的用户
 */
@Getter
@Setter
@ToString
@TableName("sys_role_user")
public class SysRoleUser extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

    /**
     * 组Id
     */
    private Long roleId;

    /**
     * 用户Id
     */
    private Long userId;
}
