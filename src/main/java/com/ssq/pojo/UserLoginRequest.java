package com.ssq.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NotBlank(message = "用户名不能为空")
    String username;
    @NotBlank(message = "密码不能为空")
    String password;
    String code;
}
