package com.xxx.myspringboot.dto.input.SysRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "更新组权限所需参数")
public class SysRoleUpdatePermitInput {
    @NotNull
    @Schema(description = "角色Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long roleId;

    @NotBlank
    @Schema(description = "权限Ids，英文逗号分割", requiredMode = Schema.RequiredMode.REQUIRED)
    private String permits;
}
