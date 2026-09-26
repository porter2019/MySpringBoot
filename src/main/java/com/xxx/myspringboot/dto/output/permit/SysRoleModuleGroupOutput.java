package com.xxx.myspringboot.dto.output.permit;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysRoleModuleGroupOutput {
    private String moduleName;
    private List<SysRoleHandlerGroupOutput> handlerList = new ArrayList<>();

    public SysRoleModuleGroupOutput(String moduleName) {
        this.moduleName = moduleName;
    }
}