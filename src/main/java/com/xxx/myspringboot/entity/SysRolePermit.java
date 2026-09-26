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
