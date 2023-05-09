package com.cuit.alias.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.alias.entity.UserRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lisihan
 */
public interface IUserRoleService extends IService<UserRole> {
    /**
     * 为用户添加角色
     * @param userId 用户id
     * @param roleId 角色id
     */
    void addUserRole(Long userId,Long roleId);
}
