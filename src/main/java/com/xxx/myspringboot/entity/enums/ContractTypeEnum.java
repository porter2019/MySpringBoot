package com.xxx.myspringboot.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xxx.myspringboot.common.IOptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 合同类型 枚举
 */
@Getter
@AllArgsConstructor
public enum ContractTypeEnum implements IOptionEnum {
    Buy(1, "采购合同"),
    Sell(2, "销售合同");

    @EnumValue
    private final Integer code;
    private final String desc;

    @JsonValue
    public Integer getCode() {
        return code;
    }

    @JsonCreator
    public static ContractTypeEnum of(Integer code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("非法的枚举类型: " + code));
    }
}
