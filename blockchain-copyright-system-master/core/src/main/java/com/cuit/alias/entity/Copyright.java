package com.cuit.alias.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lisihan
 * @Description 版权实体类
 * @date 2023/1/31-19:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_copyright")
public class Copyright implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 版权名
     */
    private String copyrightName;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 分类
     */
    private String label;

    /**
     * 数字水印
     */
    private String digitalMark;
    /**
     * 版权文件hash
     */
    private String hash;
    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
