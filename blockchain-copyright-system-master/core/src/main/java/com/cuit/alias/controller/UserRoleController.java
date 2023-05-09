package com.cuit.alias.controller;


import com.cuit.alias.common.result.CommonResult;
import com.cuit.alias.common.result.ResultUtils;
import com.cuit.alias.service.IUserRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lisihan
 */
@RestController
@RequestMapping("/user-role")
public class UserRoleController {
    @Resource
    IUserRoleService userRoleService;

    @PostMapping("/addUserRole")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> addUserRole(@NotNull(message = "id不能为空") @RequestParam("userId") Long userId,@NotNull(message = "id不能为空") @RequestParam("roleId") Long roleId) {
        userRoleService.addUserRole(userId,roleId);
        return ResultUtils.success();
    }

}

