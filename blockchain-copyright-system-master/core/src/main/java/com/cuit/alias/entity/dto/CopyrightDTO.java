package com.cuit.alias.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/2/1-16:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyrightDTO {
    /**
     * 版权名
     */
    @NotBlank(message = "版权名不能为空")
    private String copyrightName;

    /**
     * 拥有者
     */
    @NotBlank(message = "拥有者不能为空")
    private String owner;

    /**
     * 分类
     */
    @NotBlank(message = "类型不能为空")
    private String label;

    /**
     * 数字水印
     */
    @NotBlank(message = "数字水印不能为空")
    private String digitalMark;
    /**
     * 版权文件hash
     */
    @NotBlank(message = "哈希不能为空")
    private String hash;
}
