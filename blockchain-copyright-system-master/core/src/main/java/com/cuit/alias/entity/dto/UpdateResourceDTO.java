package com.cuit.alias.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/1/22-15:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResourceDTO {
    private Long id;
    /**
     * 资源路径
     */
    @NotBlank(message = "资源路径不能为空")
    private String uri;

    /**
     * 权限字段
     */
    @NotBlank(message = "权限字段不能为空")
    private String value;

    /**
     * 资源描述
     */
    @NotBlank(message = "描述不能为空")
    private String description;
}
