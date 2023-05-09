package com.cuit.alias.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/2/1-17:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCopyrightDTO {
    private Long id;

    /**
     * 版权名
     */
    @NotBlank(message = "版权名不能为空")
    private String copyrightName;


    /**
     * 分类
     */
    @NotBlank(message = "标签不能为空")
    private String label;


}
