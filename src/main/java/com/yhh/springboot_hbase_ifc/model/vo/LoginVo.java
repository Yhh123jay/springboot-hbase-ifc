package com.yhh.springboot_hbase_ifc.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录成功返回的响应结果实体类")
public class LoginVo {
    // 返回给前端的数据
    @Schema(description = "token令牌")
    private String token;
}
