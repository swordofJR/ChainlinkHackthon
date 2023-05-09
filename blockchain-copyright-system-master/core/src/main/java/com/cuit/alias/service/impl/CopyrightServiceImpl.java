package com.cuit.alias.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.entity.Copyright;
import com.cuit.alias.entity.Resource;
import com.cuit.alias.entity.User;
import com.cuit.alias.entity.dto.CopyrightDTO;
import com.cuit.alias.entity.dto.UpdateCopyrightDTO;
import com.cuit.alias.mapper.CopyrightMapper;
import com.cuit.alias.service.ICopyrightService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/1/31-19:42
 */
@Service
public class CopyrightServiceImpl extends ServiceImpl<CopyrightMapper, Copyright> implements ICopyrightService {
    @Override
    public void addCopyright(CopyrightDTO copyrightDTO) {
        // TODO 优化判断逻辑 数字水印等
        LambdaQueryWrapper<Copyright> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Copyright::getHash,copyrightDTO.getHash());
        Copyright copyrightInfo = baseMapper.selectOne(wrapper);
        if (copyrightInfo != null) {
            throw new AppException("该版权已存在");
        }
        Copyright copyright = new Copyright();
        BeanUtils.copyProperties(copyrightDTO, copyright);
        try {
            baseMapper.insert(copyright);
            // TODO 调用合约接口
        }catch (Exception e) {
            throw new AppException("添加失败");
        }
    }

    @Override
    public void updateCopyright(UpdateCopyrightDTO updateCopyrightDTO) {
        try {
            Copyright copyright = new Copyright();
            BeanUtils.copyProperties(updateCopyrightDTO, copyright);
            UpdateWrapper<Copyright> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", updateCopyrightDTO.getId());
            baseMapper.update(copyright, wrapper);
            // TODO 调用合约接口
        }catch (Exception e) {
            throw new AppException("修改失败");
        }
    }

    @Override
    public PageResult showAllCopyrights(int current,int pageSize,String copyrightName,String owner,String label) {
        Page<Copyright> page = new Page<>(current, pageSize);
        QueryWrapper<Copyright> wrapper = new QueryWrapper<>();
        if (copyrightName == null && label == null && owner == null) {
            wrapper.select("id", "copyright_name", "owner", "label");
        }else if (copyrightName != null && label == null && owner == null){
            wrapper.eq("copyright_name",copyrightName).select("id", "copyright_name", "owner", "label");
        }else  if(label != null && copyrightName == null && owner == null){
            wrapper.eq("label",label).select("id", "copyright_name", "owner", "label");
        }else if (owner != null && copyrightName == null && label == null){
            wrapper.eq("owner",owner).select("id", "copyright_name", "owner", "label");
        }else if (owner != null && copyrightName != null && label == null){
            wrapper.eq("owner",owner).eq("copyright_name",copyrightName).select("id", "copyright_name", "owner", "label");
        }else if (owner != null && copyrightName == null && label != null){
            wrapper.eq("owner",owner).eq("label",label).select("id", "copyright_name", "owner", "label");
        }else if (owner == null && copyrightName != null && label != null){
            wrapper.eq("copyright_name",copyrightName).eq("label",label).select("id", "copyright_name", "owner", "label");
        }else if(copyrightName != null && label != null && owner != null){
            wrapper.eq("owner",owner).eq("copyright_name",copyrightName).eq("label",label).select("id", "copyright_name", "owner", "label");
        }
        Page<Copyright> selectPage = baseMapper.selectPage(page, wrapper);
        PageResult result = null;
        // TODO 调用合约接口
        if (selectPage != null) {
            if (selectPage.getRecords().size() != 0 && current > 0) {
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
    public void deleteCopyright(Long id) {
        try {
            QueryWrapper<Copyright> wrapper = new QueryWrapper<>();
            wrapper.eq("id", id);
            Copyright copyright = baseMapper.selectOne(wrapper);
            if (copyright == null) {
                throw new AppException("该资源不存在");
            }
            baseMapper.deleteById(copyright.getId());
            // TODO 调用合约接口
        } catch (Exception e) {
            throw new AppException("删除失败");
        }
    }

    @Override
    public PageResult findMyCopyright(String owner, int currentPage,int pageSize,String copyrightName,String label) {
        Page<Copyright> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Copyright> wrapper = new QueryWrapper<>();
        if (copyrightName == null && label == null) {
            wrapper.eq("owner", owner).select("id", "copyright_name", "owner", "label");
        }else if (copyrightName != null && label == null){
            wrapper.eq("owner", owner).eq("copyright_name",copyrightName).select("id", "copyright_name", "owner", "label");
        }else  if(label != null && copyrightName ==null){
            wrapper.eq("owner", owner).eq("label",label).select("id", "copyright_name", "owner", "label");
        }else if(copyrightName != null && label != null){
            wrapper.eq("owner", owner).eq("copyright_name",copyrightName).eq("label",label).select("id", "copyright_name", "owner", "label");
        }
        Page<Copyright> selectPage = baseMapper.selectPage(page, wrapper);
        PageResult result = null;
        // TODO 调用合约接口
        if (selectPage != null) {
            if (selectPage.getRecords().size() != 0 && currentPage > 0) {
                result = new PageResult(selectPage);
            } else {
                throw new AppException("查无此人或索引越界");
            }
        } else {
            throw new AppException("查询失败");
        }
        return result;
    }
}
