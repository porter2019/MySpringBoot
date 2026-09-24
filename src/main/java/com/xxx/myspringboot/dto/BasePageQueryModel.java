package com.xxx.myspringboot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 所有分页查询需要继承
 */
@Setter
@Getter
@ToString
public class BasePageQueryModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页信息
     */
    @NotNull
    private PageOptions pageInfo;

}
