package com.cuit.alias.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.entity.Resource;
import com.cuit.alias.entity.Roles;
import com.cuit.alias.entity.dto.ResourceDTO;
import com.cuit.alias.entity.dto.UpdateResourceDTO;
import com.cuit.alias.mapper.ResourceMapper;
import com.cuit.alias.service.IResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lisihan
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Override
    public PageResult showAllResources(int currentPage) {
        Page<Resource> page = new Page<>(currentPage, 10);
        QueryWrapper<Resource> wrapper = new QueryWrapper<>();
        wrapper.select("id","uri", "value", "description", "create_time", "update_time");
        Page<Resource> selectPage = baseMapper.selectPage(page, wrapper);
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
    public void addResource(ResourceDTO resourceDTO) {
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resource::getValue,resourceDTO.getValue());
        Resource resourceInfo = baseMapper.selectOne(wrapper);
        if (resourceInfo != null) {
            throw new AppException("该资源已存在");
        }
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceDTO, resource);
        try {
            baseMapper.insert(resource);
        }catch (Exception e) {
            throw new AppException("添加失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResource(UpdateResourceDTO updateResourceDTO) {
        try {
            Resource resource = new Resource();
            BeanUtils.copyProperties(updateResourceDTO, resource);
            UpdateWrapper<Resource> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", updateResourceDTO.getId());
            baseMapper.update(resource, wrapper);
        }catch (Exception e) {
            throw new AppException("修改失败");
        }
    }

    @Override
    public void deleteResource(Long id) {
        try {
            QueryWrapper<Resource> wrapper = new QueryWrapper<>();
            wrapper.eq("id", id);
            Resource resource = baseMapper.selectOne(wrapper);
            if (resource == null) {
                throw new AppException("该资源不存在");
            }
            baseMapper.deleteById(resource.getId());
        } catch (Exception e) {
            throw new AppException("删除失败");
        }
    }
}
