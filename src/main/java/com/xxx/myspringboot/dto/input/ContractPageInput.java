package com.xxx.myspringboot.dto.input;

import com.xxx.myspringboot.dto.BasePageQueryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ContractPageInput extends BasePageQueryModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private Integer cType;

    private Integer flag;

}
