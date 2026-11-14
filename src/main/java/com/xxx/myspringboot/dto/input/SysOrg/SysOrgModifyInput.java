package com.xxx.myspringboot.dto.input.SysOrg;

import com.xxx.myspringboot.dto.input.BaseTreeModifyInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部门修改所需
 */
@Getter
@Setter
@ToString
public class SysOrgModifyInput extends BaseTreeModifyInput {
    private static final long serialVersionUID = 1L;

    private Long leaderId;

    private String leaderName;
}
