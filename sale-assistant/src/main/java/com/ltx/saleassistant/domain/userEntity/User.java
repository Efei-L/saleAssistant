package com.ltx.saleassistant.domain.userEntity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TableName(value = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @TableId
    private Integer id;

    @Length(max=20, message = "用户名长度不能超过20")
    @NotNull(message  = "用户名称不能为空！")
    @NotBlank
    private String userName;

    @Length(max=16, message = "密码长度不能超过16")
    @NotNull(message  = "密码不能为空！")
    @NotBlank
    private String password;

    @Length(max=5, message = "权限信息不能超过5")
    @NotNull(message  = "权限不能为空！")
    @NotBlank
    private String role;



}
