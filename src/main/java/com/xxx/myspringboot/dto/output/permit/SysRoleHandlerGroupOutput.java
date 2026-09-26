package com.xxx.myspringboot.dto.output.permit;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysRoleHandlerGroupOutput {
    private String handlerName;
    private List<SysRolePermitOutput> permitList = new ArrayList<>();

    public SysRoleHandlerGroupOutput(String handlerName) {
        this.handlerName = handlerName;
    }
}