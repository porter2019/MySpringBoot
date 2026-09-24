package com.xxx.myspringboot.dto.input.SysUser;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "修改头像所需参数")
public class SysUserChangeAvatarInput {

    @NotBlank
    @Schema(description = "新头像的相对地址")
    private String avatar;
}
