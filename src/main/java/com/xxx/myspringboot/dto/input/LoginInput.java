package com.xxx.myspringboot.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录所需信息
 */
@Data
@Schema(description = "登录所需信息")
public class LoginInput {

    /**
     * 手机号
     */
    @NotBlank
    @Schema(description = "手机号", example = "13000000000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cellPhone;

    /**
     * 密码
     */
    @NotBlank
    @Schema(description = "密码", example = "000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
