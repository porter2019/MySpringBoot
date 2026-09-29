package com.xxx.myspringboot.dto;

import lombok.Data;

@Data
public class AuditInfo {
    public AuditInfo(String operator, String local) {
        this.operator = operator;
        this.local = local;
    }

    private String operator;
    private String local;
}
