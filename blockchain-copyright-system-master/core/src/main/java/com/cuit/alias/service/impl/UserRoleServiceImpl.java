package com.cuit.alias.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.entity.Roles;
import com.cuit.alias.entity.UserRole;
import com.cuit.alias.mapper.UserRoleMapper;
import com.cuit.alias.service.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lisihan
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    /**
     * 为用户添加角色
     * @param userId 用户id
     * @param roleId 角色id
     */
    @Override
    public void addUserRole(Long userId,Long roleId) {
        try {
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId).eq("role_id",roleId);
            UserRole userRole = baseMapper.selectOne(wrapper);
            if (userRole != null) {
                throw new AppException("该角色已绑定这个角色");
            }
            userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            baseMapper.insert(userRole);
        }catch (Exception e){
            throw new AppException("角色添加失败");
        }
    }
}
