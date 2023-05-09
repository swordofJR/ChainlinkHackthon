package com.cuit.alias.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.entity.Resource;
import com.cuit.alias.entity.dto.ResourceDTO;
import com.cuit.alias.entity.dto.UpdateResourceDTO;
import com.cuit.alias.entity.dto.UpdateRoleDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lisihan
 */
public interface IResourceService extends IService<Resource> {
    /**
     * 获取所有资源
     * @param currentPage 当前页数
     * @return 分页结果集
     */
    PageResult showAllResources(int currentPage);

    /**
     * 添加资源
     * @param resourceDTO
     */
    void addResource(ResourceDTO resourceDTO);

    /**
     * 修改资源
     * @param updateResourceDTO
     */
    void updateResource(UpdateResourceDTO updateResourceDTO);

    /**
     * 删除资源
     * @param id
     */
    void deleteResource(Long id);
}
