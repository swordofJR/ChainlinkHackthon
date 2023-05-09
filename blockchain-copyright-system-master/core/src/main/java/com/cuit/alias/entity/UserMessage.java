package com.cuit.alias.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/1/19-21:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;
    /**
     * 性别
     */
    private Integer sex;
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

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色的英文名
     */
    private String value;

    /**
     * 资源路径
     */
    private String uri;

    /**
     * 权限字段
     */
    private String reValue;
}
