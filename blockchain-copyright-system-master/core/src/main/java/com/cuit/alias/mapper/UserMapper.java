package com.cuit.alias.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.alias.entity.Resource;
import com.cuit.alias.entity.Roles;
import com.cuit.alias.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lisihan
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 得到用户的角色列表
     *
     * @param userId 用户id
     * @return {@code List<Roles>}
     */
    List<Roles> getUserRoles(@Param("userId") Long userId);

    /**
     * 获取用户资源
     *
     * @param roleId 角色id
     * @return {@code List<Resource>}
     */
    List<Resource> getUserResources(@Param("roleId") Long roleId);

}
