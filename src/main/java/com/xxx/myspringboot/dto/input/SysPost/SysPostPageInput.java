package com.xxx.myspringboot.dto.input.SysPost;

import com.xxx.myspringboot.dto.BasePageQueryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 岗位 分页查询输入参数
 */
@Getter
@Setter
@ToString
public class SysPostPageInput extends BasePageQueryModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String code;
}
