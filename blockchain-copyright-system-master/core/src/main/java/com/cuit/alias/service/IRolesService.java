package com.cuit.alias.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.entity.Roles;
import com.cuit.alias.entity.dto.UpdateRoleDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lisihan
 */
public interface IRolesService extends IService<Roles> {
    /**
     * 获取所有角色
     * @param currentPage 当前页数
     * @return 分页结果集
     */
    PageResult showAllRoles(int currentPage);

    /**
     * 添加角色
     * @param roleName 角色名
     * @param roleValue 角色英文名
     */
    void addRole(String roleName,String roleValue);

    /**
     * 修改角色
     * @param updateRoleDTO
     */
    void updateRole(UpdateRoleDTO updateRoleDTO);

    /**
     * 删除角色
     * @param id
     */
    void deleteRole(Long id);
}
