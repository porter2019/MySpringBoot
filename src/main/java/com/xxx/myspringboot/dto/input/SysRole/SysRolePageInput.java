package com.xxx.myspringboot.dto.input.SysRole;

import com.xxx.myspringboot.dto.BasePageQueryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SysRolePageInput extends BasePageQueryModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
}
