package com.cuit.alias.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/1/22-15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDTO {
    private Long id;

    /**
     * 角色名
     */
    @NotBlank(message = "角色名不能为空")
    private String name;

    /**
     * 角色的英文名
     */
    @NotBlank(message = "角色英文名不能为空")
    private String value;
}
