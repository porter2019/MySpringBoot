package com.xxx.myspringboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 分页查询公共参数
 */
@Getter
@Setter
@ToString
public class PageOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @NotBlank
    private Integer pageIndex;

    /**
     * 每页大小
     */
    @NotBlank
    private Integer pageSize;

    /**
     * 排序: id desc
     */
    private String orderBy;
}
