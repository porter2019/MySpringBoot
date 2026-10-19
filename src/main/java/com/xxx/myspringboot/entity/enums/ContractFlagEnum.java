package com.xxx.myspringboot.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xxx.myspringboot.common.IOptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 合同业务状态
 */
@Getter
@AllArgsConstructor
public enum ContractFlagEnum implements IOptionEnum {
    Draft(1, "草稿"),
    Pending(2, "审批中"),
    Pass(3, "审批通过"),
    Reject(4, "审批不通过");

    @EnumValue
    private final Integer code;
    private final String desc;


    @JsonValue
    public Integer getCode() {
        return code;
    }

    @JsonCreator
    public static ContractFlagEnum of(Integer code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("非法的枚举类型: " + code));
    }

}
