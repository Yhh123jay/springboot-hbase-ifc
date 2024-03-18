package com.yhh.springboot_hbase_ifc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "登录请求实体类")
@Data
public class LoginDto {
    // 登录用户名
    @Schema(description = "登录用户名")
    private String username;
    // 登录密码
    @Schema(description = "登录密码")
    private String password;
}
