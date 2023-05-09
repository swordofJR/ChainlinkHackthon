package com.cuit.alias.controller;


import com.cuit.alias.common.result.CommonResult;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.common.result.ResultUtils;
import com.cuit.alias.entity.dto.UpdateRoleDTO;
import com.cuit.alias.service.IRolesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/roles")
@Validated
public class RolesController {
    @Resource
    IRolesService rolesService;

    @GetMapping("/getRoles")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<PageResult> getRoles(@NotNull(message = "页数不能为空") @RequestParam("currentPage") int currentPage) {
        return ResultUtils.success(rolesService.showAllRoles(currentPage));
    }

    @PostMapping("/addRole")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> addRole(@RequestParam("roleName") String roleName,@RequestParam("roleValue") String roleValue) {
        rolesService.addRole(roleName,roleValue);
        return ResultUtils.success();
    }

    @PutMapping("/updateRole")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> updateRole(@Valid @RequestBody UpdateRoleDTO updateRoleDTO) {
        rolesService.updateRole(updateRoleDTO);
        return ResultUtils.success();
    }

    @DeleteMapping("/deleteRole")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> deleteRole(@NotNull(message = "id不能为空") @RequestParam("id") Long id) {
        rolesService.deleteRole(id);
        return ResultUtils.success();
    }
}

