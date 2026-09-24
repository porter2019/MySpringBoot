package com.xxx.myspringboot.dto.input.SysUser;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改密码所需")
public class SysUserChangePwdInput {

    @NotBlank()
    @Schema(description = "旧密码", example = "000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;

    @NotBlank()
    @Schema(description = "新密码", example = "000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
