package com.cuit.alias.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.alias.entity.RoleResource;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lisihan
 */
public interface IRoleResourceService extends IService<RoleResource> {

    /**
     * 为角色添加资源
     * @param roleId 角色id
     * @param resourceId 资源id
     */
    void addRoleResource(Long roleId,Long resourceId);
}
