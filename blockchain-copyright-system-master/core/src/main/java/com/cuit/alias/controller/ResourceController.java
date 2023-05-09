package com.cuit.alias.controller;


import com.cuit.alias.common.result.CommonResult;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.common.result.ResultUtils;
import com.cuit.alias.entity.dto.ResourceDTO;
import com.cuit.alias.entity.dto.UpdateResourceDTO;
import com.cuit.alias.entity.dto.UpdateRoleDTO;
import com.cuit.alias.service.IResourceService;
import com.cuit.alias.service.IRolesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lisihan
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    IResourceService resourceService;

    @GetMapping("/getResources")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<PageResult> getResources(@NotNull(message = "页数不能为空") @RequestParam("currentPage") int currentPage) {
        return ResultUtils.success(resourceService.showAllResources(currentPage));
    }

    @PostMapping("/addResource")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> addResource(@Valid @RequestBody ResourceDTO resourceDTO) {
        resourceService.addResource(resourceDTO);
        return ResultUtils.success();
    }

    @PutMapping("/updateResource")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> updateResource(@Valid @RequestBody UpdateResourceDTO updateResourceDTO) {
        resourceService.updateResource(updateResourceDTO);
        return ResultUtils.success();
    }

    @DeleteMapping("/deleteResource")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> deleteResource(@NotNull(message = "id不能为空") @RequestParam("id") Long id) {
        resourceService.deleteResource(id);
        return ResultUtils.success();
    }
}

