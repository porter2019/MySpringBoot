package com.xxx.myspringboot.entity.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 标准树型实体需要继承的，包括创建人、修改人等信息
 */
@Getter
@Setter
@ToString
public class BaseEntityTree extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

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
     * 完整Id
     */
    private String fullId;

    /**
     * 完整名称
     */
    private String fullName;

    /**
     * 完整类别层级排序
     */
    private String fullOrderNo;

    /**
     * 层级
     */
    private Integer levelNo;
}
