package com.cuit.alias.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.entity.Resource;
import com.cuit.alias.entity.User;
import com.cuit.alias.entity.dto.LoginDTO;
import com.cuit.alias.entity.dto.UpdateInformationDTO;
import com.cuit.alias.entity.dto.UserDTO;


import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lisihan
 */
public interface IUserService extends IService<User> {
    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return {@code User}
     */
    User getUserByUsername(String username);

    /**
     * 得到当前用户的权限列表
     *
     * @param userId 用户id
     * @return {@code List<Resource>}
     */
    List<Resource> getUserPermission(Long userId);

    /**
     * 用户注册
     *
     * @param userDTO 用户dto
     * @return boolean
     */
    boolean registry(UserDTO userDTO);

    /**
     * 登录
     * @param loginDTO 登录DTO
     * @return
     */
    String login(LoginDTO loginDTO);

    /**
     * 展示所有用户
     * @param currentPage 当前页数
     * @return 分页结果集
     */
    PageResult showUsers(int currentPage);

    /**
     * 通过用户名进行模糊查询
     * @param username 用户名
     * @param currentPage 当前页数
     * @return 分页返回结果集
     */
    PageResult fuzzySearch(String username, int currentPage);


    /**
     * 修改用户信息
     * @param updateInformationDTO 待修改的用户信息
     */
    void updateInformation(UpdateInformationDTO updateInformationDTO);

    /**
     * 修改性别
     * @param sex 性别
     */
    void updateSex(String sex);

    /**
     * 用户忘记密码
     * @param password 新密码
     * @param emailCode 邮箱验证码
     * @param username 用户名
     * @param email 邮箱
     */
    void forgetPassword(String password,String emailCode,String username,String email);

    /**
     * 用户修改密码
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     */
    void updatePassword(String newPassword,String oldPassword);

    /**
     * 超管重置用户密码
     * @param userId 用户id
     */
    void resetPassword(int userId);

    /**
     * 超管修改用户邮箱
     * @param newEmail 新邮箱
     * @param userId 用户ID
     */
    void updateEmail(String newEmail,int userId);

    /**
     * 超管删除用户
     * @param userId 用户id
     */
    void deleteUser(int userId);

    /**
     * 测试用
     * @return
     */
    User testService();
}
