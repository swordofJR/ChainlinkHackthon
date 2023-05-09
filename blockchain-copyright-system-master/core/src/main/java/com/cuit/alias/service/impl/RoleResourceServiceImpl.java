package com.cuit.alias.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.entity.RoleResource;
import com.cuit.alias.entity.UserRole;
import com.cuit.alias.mapper.RoleResourceMapper;
import com.cuit.alias.service.IRoleResourceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lisihan
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements IRoleResourceService {

    @Override
    public void addRoleResource(Long roleId, Long resourceId) {
        try {
            QueryWrapper<RoleResource> wrapper = new QueryWrapper<>();
            wrapper.eq("resource_id", resourceId).eq("role_id",roleId);
            RoleResource roleResource = baseMapper.selectOne(wrapper);
            if (roleResource != null) {
                throw new AppException("该角色已绑定这个角色");
            }
            roleResource = new RoleResource();
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(resourceId);
            baseMapper.insert(roleResource);
        }catch (Exception e){
            throw new AppException("角色添加失败");
        }
    }
}
