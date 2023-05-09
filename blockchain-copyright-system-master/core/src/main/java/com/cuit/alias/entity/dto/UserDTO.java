package com.cuit.alias.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lisihan
 * @version 1.0
 **/
@Data
public class UserDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @Length(min = 6, max = 255, message = "密码长度不得小于6位")
    private String password;
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "电话不能为空")
    private String phone;
    @NotNull(message = "性别不能为空")
    private Integer sex;
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
    @NotBlank(message = "邮箱验证码不能为空")
    private String emailCode;
    private String key;

}
