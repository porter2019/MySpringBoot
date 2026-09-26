package com.xxx.myspringboot.dto.output.permit;

import lombok.Data;

/**
 * 用户组权限
 */
@Data
public class SysRolePermitOutput {
    private String moduleName;
    private String handlerName;
    private Integer permitId;
    private String permitName;
    private String aliasName;
    private Integer orderNo;
    private Boolean isChecked;
}
