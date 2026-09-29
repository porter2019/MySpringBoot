package com.xxx.myspringboot.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.myspringboot.common.IOptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumOptionUtils {
    public static List<EnumGroupVO> buildEnumList(
            List<Class<? extends Enum<?>>> classes,
            List<String> keys) {

        if (classes == null || keys == null || classes.size() != keys.size()) {
            throw new IllegalArgumentException("classes 与 keys 长度必须一致");
        }

        List<EnumGroupVO> result = new ArrayList<>(classes.size());
        for (int i = 0; i < classes.size(); i++) {
            result.add(new EnumGroupVO(keys.get(i), toOptions(classes.get(i))));
        }
        return result;
    }

    private static List<OptionVO> toOptions(Class<? extends Enum<?>> clazz) {
        if (!IOptionEnum.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getName() + " 未实现 IOptionEnum");
        }
        return Arrays.stream(clazz.getEnumConstants())
                .map(e -> {
                    IOptionEnum oe = (IOptionEnum) e;
                    return new OptionVO(oe.getDesc(), oe.getCode());
                })
                .toList();
    }


    @Data
    @AllArgsConstructor
    public static class EnumGroupVO {
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Options")
        private List<OptionVO> options;
    }

    @Data
    @AllArgsConstructor
    public static class OptionVO {
        @JsonProperty("Label")
        private String label;
        @JsonProperty("Value")
        private Object value;
    }
}
