package com.xxx.myspringboot.dto.input.SysDirectory;

import com.xxx.myspringboot.dto.BasePageQueryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 字典项分页查询
 */
@Getter
@Setter
@ToString
public class SysDirectoryItemFullViewPageInput extends BasePageQueryModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long categoryId;

    private String name;
}
