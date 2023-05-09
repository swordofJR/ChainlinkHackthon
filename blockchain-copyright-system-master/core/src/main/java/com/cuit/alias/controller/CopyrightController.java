package com.cuit.alias.controller;

import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.common.result.CommonResult;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.common.result.ResultUtils;
import com.cuit.alias.common.utils.HashUtils;
import com.cuit.alias.common.utils.UserHolder;
import com.cuit.alias.entity.User;
import com.cuit.alias.entity.dto.CopyrightDTO;
import com.cuit.alias.entity.dto.UpdateCopyrightDTO;
import com.cuit.alias.entity.dto.UpdateResourceDTO;
import com.cuit.alias.service.ICopyrightService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static com.cuit.alias.common.constant.CommonConstants.*;

/**
 * @author lisihan
 * @Description TODO
 * @date 2023/2/1-17:17
 */
@RestController
@RequestMapping("/copyright")
@Validated
public class CopyrightController {
    @Resource
    ICopyrightService copyrightService;

    @PostMapping("/addCopyright")
    public CommonResult<String> addCopyright(@RequestParam("file") MultipartFile file,@RequestParam("copyrightName") String copyrightName,@RequestParam("label") String label) {
        String filename = file.getOriginalFilename().toLowerCase();
        if(filename.endsWith(PNG)||filename.endsWith(JPG)||filename.endsWith(PDF)||filename.endsWith(MP3)||filename.endsWith(MP4)){
            try {
                byte [] byteArr=file.getBytes();
                InputStream inputStream = new ByteArrayInputStream(byteArr);
                String hash = HashUtils.md5HashCode32(inputStream);
                User user = UserHolder.getUser();
                // TODO 数字水印
                CopyrightDTO copyrightDTO = new CopyrightDTO(copyrightName, user.getUsername(), label, "xxxx", hash);
                copyrightService.addCopyright(copyrightDTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            throw new AppException("文件类型不符或读取失败");
        }
        return ResultUtils.success();
    }
    @GetMapping("/showCopyrights")
//    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<PageResult> showCopyrights(@NotNull(message = "页数不能为空") @RequestParam("current") int current,@RequestParam("pageSize")int pageSize,String copyrightName,String owner,String label) {
        return ResultUtils.success(copyrightService.showAllCopyrights(current,pageSize,copyrightName,owner,label));
    }


    @GetMapping("/showMyCopyright")
    public CommonResult<PageResult> showMyCopyright(@NotNull(message = "页数不能为空") @RequestParam("current") int current,@RequestParam("pageSize")int pageSize,String copyrightName,String label) {

        User user = UserHolder.getUser();
        return ResultUtils.success(copyrightService.findMyCopyright(user.getUsername(),current,pageSize,copyrightName,label));
    }
    @PutMapping("/updateCopyright")
    public CommonResult<String> updateCopyright(@Valid @RequestBody UpdateCopyrightDTO updateCopyrightDTO) {
        copyrightService.updateCopyright(updateCopyrightDTO);
        return ResultUtils.success();
    }
    @DeleteMapping("/deleteCopyright")
    public CommonResult<String> deleteCopyright(@NotNull(message = "id不能为空") @RequestParam("id") Long id) {
        copyrightService.deleteCopyright(id);
        return ResultUtils.success();
    }
//    @GetMapping("/showAllCopyrights")
//    public CommonResult<PageResult> showAllCopyrights(@NotNull(message = "页数不能为空") @RequestParam("currentPage") int currentPage){
//        return ResultUtils.success(copyrightService.showAllCopyrights(currentPage));
//    }
}
