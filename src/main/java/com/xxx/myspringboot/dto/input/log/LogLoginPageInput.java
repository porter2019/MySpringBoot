package com.xxx.myspringboot.dto.input.log;

import com.xxx.myspringboot.dto.BasePageQueryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class LogLoginPageInput extends BasePageQueryModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clientType;

    private String userName;

    private String cellPhone;

    private Integer userId;
}
