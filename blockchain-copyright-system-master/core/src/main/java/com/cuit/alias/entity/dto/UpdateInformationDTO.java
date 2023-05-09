package com.cuit.alias.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/1/19-22:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInformationDTO {

    /**
     * 电话号码
     */
    @NotNull(message = "电话号码不能为空")
    private String phone;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(max = 15, message = "昵称长度不能大于15")
    @Size(min = 2, message = "昵称长度不能小于2")
    private String username;


}
