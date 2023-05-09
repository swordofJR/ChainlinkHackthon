package com.cuit.alias.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.entity.Roles;
import com.cuit.alias.entity.User;
import com.cuit.alias.entity.dto.UpdateRoleDTO;
import com.cuit.alias.mapper.RolesMapper;
import com.cuit.alias.service.IRolesService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.cuit.alias.common.constant.RedisConstants.LOGIN_USER_KEY;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lisihan
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements IRolesService {


    @Override
    public PageResult showAllRoles(int currentPage) {
        Page<Roles> page = new Page<>(currentPage, 10);
        QueryWrapper<Roles> wrapper = new QueryWrapper<>();
        wrapper.select("id","name", "value", "user_count", "create_time", "update_time");
        Page<Roles> selectPage = baseMapper.selectPage(page, wrapper);
        PageResult result = null;
        if (selectPage != null) {
            if (selectPage.getRecords().size() != 0 && currentPage > 0) {
                result = new PageResult(selectPage);
            } else {
                throw new AppException("索引越界");
            }
        } else {
            throw new AppException("查询失败");
        }
        return result;
    }

    @Override
    public void addRole(String roleName, String roleValue) {
        QueryWrapper<Roles> wrapper = new QueryWrapper<>();
        wrapper.eq("name", roleName);
        Roles roles = baseMapper.selectOne(wrapper);
        if (roles != null) {
            throw new AppException("该角色已存在");
        }
        roles = new Roles();
        roles.setName(roleName);
        roles.setValue(roleValue);
        try {
            baseMapper.insert(roles);
        }catch (Exception e) {
            throw new AppException("添加失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(UpdateRoleDTO updateRoleDTO) {
        try {
            Roles roles = new Roles();
            BeanUtils.copyProperties(updateRoleDTO, roles);
            UpdateWrapper<Roles> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", updateRoleDTO.getId());
            baseMapper.update(roles, wrapper);
        }catch (Exception e) {
            throw new AppException("修改失败");
        }
    }

    @Override
    public void deleteRole(Long id) {
        try {
            QueryWrapper<Roles> wrapper = new QueryWrapper<>();
            wrapper.eq("id", id);
            Roles roles = baseMapper.selectOne(wrapper);
            if (roles == null) {
                throw new AppException("该角色不存在");
            }
            baseMapper.deleteById(roles.getId());
        } catch (Exception e) {
            throw new AppException("删除失败");
        }
    }


}
