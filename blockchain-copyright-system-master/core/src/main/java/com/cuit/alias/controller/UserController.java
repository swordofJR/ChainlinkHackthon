package com.cuit.alias.controller;


import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.common.result.CommonResult;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.common.result.ResultUtils;
import com.cuit.alias.common.utils.RedisServiceImpl;
import com.cuit.alias.common.utils.UserHolder;
import com.cuit.alias.entity.User;
import com.cuit.alias.entity.dto.LoginDTO;
import com.cuit.alias.entity.dto.UpdateInformationDTO;
import com.cuit.alias.entity.dto.UserDTO;
import com.cuit.alias.service.IUserService;
import com.wf.captcha.SpecCaptcha;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

import static com.cuit.alias.common.constant.RedisConstants.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lisihan
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    IUserService userService;
    @Resource
    RedisServiceImpl redisService;
    @Resource
    JavaMailSenderImpl mailSender;

    @PostMapping("/register")
    public CommonResult<String> registry(@Valid @RequestBody UserDTO userDTO) {
        userService.registry(userDTO);
        return ResultUtils.success();
    }

    @PostMapping("/login")
    public CommonResult<String> login(@NotBlank(message = "用户名不能为空") @RequestParam String username,
                                      @Length(min = 6, max = 255, message = "密码长度不能小于6位") @RequestParam String password,@RequestParam String verifyCode,@RequestParam String key) {
        LoginDTO loginDTO = new LoginDTO(username, password, verifyCode,key);
        return ResultUtils.success(userService.login(loginDTO));
    }
    @GetMapping("/getPermissions")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<List> getPermissions(@NotNull(message = "用户id不能为空") @RequestParam Long userId) {
        return ResultUtils.success(userService.getUserPermission(userId));
    }

    /**
     * 获取验证码
     * @param response HttpServletResponse
     */
    @GetMapping("/getCode")
    public CommonResult<String> getCode(HttpServletResponse response, String key){
        redisService.del(VERIFY_KEY+key);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        SpecCaptcha captcha = new SpecCaptcha(120, 40);
        captcha.setLen(4);
        String text = captcha.text();
        System.out.println("图片验证码"+text);
        redisService.set(VERIFY_KEY+key,text,VERIFY_KEY_TTL);
        return ResultUtils.success(captcha.toBase64());
    }

    /**
     * 获取邮箱验证码
     * @param email 邮箱地址
     * @return result
     */
    @PostMapping("/getEmailCode")
    public CommonResult<String> sendEmail(@RequestParam String email){
        redisService.del(EMAIL_VERIFY_KEY+email);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("注册验证码");
            int random = (int) (Math.random() * ((9999 - 1000 + 1) + 1000));
            String code = String.valueOf(random);
            System.out.println("生成的邮箱验证码" + code);
            //设置文本
            message.setText(code + ",有效时间5分钟");
            //发送给用户
            message.setTo(email);
            message.setFrom("1054109492@qq.com");
            mailSender.send(message);
            redisService.set(EMAIL_VERIFY_KEY+email, code, EMAIL_VERIFY_TTL);
            return ResultUtils.success(true,"发送成功");
        }catch (Exception e){
            throw new AppException("发送失败");
        }
    }
    /**
     * 用户注销
     * @param username 用户名
     * @return 结果集
     */
    @PostMapping("/logout")
    public CommonResult<String> logout(String username){
        try {
            UserHolder.removeUser();
            redisService.del(LOGIN_USER_KEY + username);
            redisService.del(username);
            return ResultUtils.success(true, "注销成功");
        }catch (Exception e) {
            throw new AppException("注销失败");
        }
    }

    @GetMapping("/showUsers")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<PageResult> showUsers(@RequestParam("currentPage") int currentPage) {
        return ResultUtils.success(userService.showUsers(currentPage));
    }
    @GetMapping("/fuzzySearch")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<PageResult> fuzzySearchUsers(@RequestParam("username") String username,@RequestParam("currentPage") int currentPage){
        return ResultUtils.success(userService.fuzzySearch(username,currentPage));
    }
    /**
     * 修改用户信息
     * @param updateInformationDTO 待修改的用户信息
     * @return 结果集
     */
    @PutMapping("/updateInformation")
    public CommonResult<String> updateInformation(@Valid @RequestBody UpdateInformationDTO updateInformationDTO){
        userService.updateInformation(updateInformationDTO);
        return ResultUtils.success();
    }

    /**
     * 修改性别
     * @param sex 性别
     * @return 结果集
     */
    @PutMapping("/updateSex")
    public CommonResult<String> updateSex(@RequestParam("sex") String sex){
        userService.updateSex(sex);
        return ResultUtils.success();
    }
    /**
     * 用户忘记密码
     * @param username 用户名
     * @param password 设置的新密码
     * @param emailCode 邮箱验证码
     * @param email 绑定的邮箱
     * @return 结果集
     */
    @PutMapping("/forgetPassword")
    public CommonResult<String> forgetPassword(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("emailCode") String emailCode,@RequestParam("email") String email){
        userService.forgetPassword(password,emailCode,username,email);
        return ResultUtils.success();
    }

    /**
     * 用户修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果集
     */
    @PutMapping("/updatePassword")
    public CommonResult<String> updatePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword){
        userService.updatePassword(newPassword,oldPassword);
        return ResultUtils.success();
    }

    /**
     * 重置用户密码
     * @param userId 用户id
     * @return 结果集
     */
    @PutMapping("/resetPassword")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> resetPassword(@RequestParam("userId") int userId){
        userService.resetPassword(userId);
        return ResultUtils.success();
    }

    /**
     * 超管修改用户邮箱
     * @param userId 用户id
     * @param newEmail 新邮箱
     * @return 结果集
     */
    @PutMapping("/updateEmail")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> updateEmail(@RequestParam("userId") int userId,@RequestParam("newEmail") String newEmail){
        userService.updateEmail(newEmail,userId);
        return ResultUtils.success();
    }

    /**
     * 超管删除用户
     * @param userId 用户id
     * @return 结果集
     */
    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> deleteUser(@RequestParam("userId") int userId){
        userService.deleteUser(userId);
        return ResultUtils.success();
    }
    @GetMapping("/getInfo")
    public CommonResult<User> getLoginInfo() {
//        User user = UserHolder.getUser();
//        System.out.println(user);
//        return ResultUtils.success(user);
        return ResultUtils.success(userService.testService());
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('superAdmin:test')")
    public CommonResult<String> test() {
        return ResultUtils.success("test");
    }

    @GetMapping("/hello")
    public CommonResult<String> hello() {
        return ResultUtils.success("hello");
    }
}

