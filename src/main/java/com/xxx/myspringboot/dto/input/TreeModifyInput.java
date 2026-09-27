package com.xxx.myspringboot.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TreeModifyInput {

    private Long id;
    /**
     * 标识
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序数字
     */
    private String orderNo;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 父级名称
     */
    private String parentName;

    /**
     * 状态
     */
    private Boolean status = true;

    /**
     * 备注
     */
    private String remark;
}
