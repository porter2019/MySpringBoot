package com.xxx.myspringboot.dto;

import lombok.Data;

import java.util.Locale;

@Data
public class AuditInfo {
    public AuditInfo(String operator, String local, String clientTag) {
        this.operator = operator;
        this.local = local;
        this.clientTag = clientTag;
    }

    private String operator;
    private String local;
    private String clientTag;
//    private Integer clientType;

    public Integer getClientType() {
        return switch (clientTag.toLowerCase(Locale.ROOT)) {
            case "om" -> 1; //后台
            case "mp" -> 2; //小程序
            default ->      //未知
                    0;
        };
    }

}
