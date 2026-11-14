package com.xxx.myspringboot.dto.input.SysUser;

import com.xxx.myspringboot.dto.BasePageQueryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SysUserPageInput extends BasePageQueryModel {

    private static final long serialVersionUID = 1L;

    private Long orgId;

    private String cellPhone;

    private String orgName;

    private String roleNames;

    private String postNames;

    private String userName;
}
