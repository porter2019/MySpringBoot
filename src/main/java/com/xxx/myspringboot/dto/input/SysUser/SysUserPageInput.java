package com.xxx.myspringboot.dto.input.SysUser;

import com.xxx.myspringboot.dto.BasePageQueryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SysUserPageInput extends BasePageQueryModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cellPhone;
}
