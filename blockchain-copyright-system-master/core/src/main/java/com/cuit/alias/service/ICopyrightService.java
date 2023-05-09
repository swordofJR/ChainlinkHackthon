package com.cuit.alias.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.entity.Copyright;
import com.cuit.alias.entity.dto.CopyrightDTO;
import com.cuit.alias.entity.dto.UpdateCopyrightDTO;
import com.cuit.alias.entity.dto.UpdateResourceDTO;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/1/31-19:41
 */
public interface ICopyrightService extends IService<Copyright> {
    /**
     * 添加版权
     * @param copyrightDTO
     */
    void addCopyright(CopyrightDTO copyrightDTO);

    /**
     * 修改基础信息
     * @param updateCopyrightDTO
     */
    void updateCopyright(UpdateCopyrightDTO updateCopyrightDTO);

    /**
     *
     * @param current
     * @param pageSize
     * @param copyrightName
     * @param owner
     * @param label
     * @return
     */
    PageResult showAllCopyrights(int current,int pageSize,String copyrightName,String owner,String label);

    /**
     * 删除版权
     * @param id
     */
    void deleteCopyright(Long id);

    /**
     * 通过用户名进行模糊查询
     * @param owner 用户名
     * @param currentPage 当前页数
     * @return 分页返回结果集
     */
    PageResult findMyCopyright(String owner, int currentPage,int pageSize,String copyrightName,String label);
}
