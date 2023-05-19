package com.ltx.saleassistant.domain.DTO;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Length(max=20, message = "用户名长度不能超过20")
    @NotNull(message  = "用户名称不能为空！")
    @NotBlank
    private String userName;

    @Length(max=16, message = "密码长度不能超过16")
    @NotNull(message  = "密码不能为空！")
    @NotBlank
    private String password;
}
