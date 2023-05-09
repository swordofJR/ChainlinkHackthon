package com.cuit.alias.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.alias.common.exception.AppException;
import com.cuit.alias.common.result.PageResult;
import com.cuit.alias.common.utils.JwtUtils;
import com.cuit.alias.common.utils.RedisService;
import com.cuit.alias.common.utils.UserHolder;
import com.cuit.alias.entity.MyUserDetails;
import com.cuit.alias.entity.Resource;
import com.cuit.alias.entity.Roles;
import com.cuit.alias.entity.User;
import com.cuit.alias.entity.dto.LoginDTO;
import com.cuit.alias.entity.dto.UpdateInformationDTO;
import com.cuit.alias.entity.dto.UserDTO;
import com.cuit.alias.mapper.UserMapper;
import com.cuit.alias.service.IUserRoleService;
import com.cuit.alias.service.IUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cuit.alias.common.constant.CommonConstants.FEMALE_SEX;
import static com.cuit.alias.common.constant.CommonConstants.MALE_SEX;
import static com.cuit.alias.common.constant.RedisConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lisihan
 */
@Service
@Log4j2
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RedisService redisService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    IUserRoleService userRoleService;

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<Resource> getUserPermission(Long userId) {
        List<Roles> roles = baseMapper.getUserRoles(userId);
        List<Resource> results = new ArrayList<>();
        for (Roles role : roles) {
            List<Resource> resources = baseMapper.getUserResources(role.getId());
            results.addAll(resources);
        }
        return results;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean registry(UserDTO userDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, userDTO.getUsername());
        User userInfo = baseMapper.selectOne(wrapper);
        if (userInfo != null) {
            throw new AppException("用户名已存在");
        }

        User user = new User();
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(userDTO.getUsername());
        if (m.find()) {
            throw new AppException("用户名中不能含有特殊字符");
        }
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        String emailCode;
        try {
            emailCode = (String) redisService.get(EMAIL_VERIFY_KEY + user.getEmail());
        } catch (Exception e) {
            throw new AppException("邮箱验证码失效或出现其他错误");
        }
        if (!emailCode.equals(userDTO.getEmailCode())) {
            redisService.del(EMAIL_VERIFY_KEY + user.getEmail());
            throw new AppException("邮箱验证码错误");
        }
        String verifyCode;
        try {
            verifyCode = (String) redisService.get(VERIFY_KEY + userDTO.getKey());
        } catch (Exception e) {
            throw new AppException("验证码失效或出现其他错误");
        }
        if (!verifyCode.equalsIgnoreCase(userDTO.getVerifyCode())) {
            redisService.del(VERIFY_KEY + userDTO.getKey());
            throw new AppException("验证码错误");
        }
        try {
            baseMapper.insert(user);
            redisService.del(VERIFY_KEY + userDTO.getKey());
            redisService.del(EMAIL_VERIFY_KEY + user.getEmail());
        } catch (Exception e) {
            throw new AppException("注册失败");
        }
        return true;
    }

    @Override
    public String login(LoginDTO loginDTO) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            MyUserDetails myUserDetails = new MyUserDetails();
            BeanUtils.copyProperties(userDetails, myUserDetails);
            User user = myUserDetails.getUser();
            String verifyCode;
            verifyCode = (String) redisService.get(VERIFY_KEY + loginDTO.getKey());
            if (verifyCode == null) {
                throw new AppException("验证码失效");
            }

            if (!verifyCode.equalsIgnoreCase(loginDTO.getVerifyCode())) {
                redisService.del(VERIFY_KEY + loginDTO.getKey());
                throw new AppException("验证码错误");
            }
            UsernamePasswordAuthenticationToken authentication = new
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = JwtUtils.generateToken(userDetails.getUsername());
            redisService.set(loginDTO.getUsername(), token, JwtUtils.getExpiration());
            redisService.set(LOGIN_USER_KEY + user.getUsername(), user, LOGIN_USER_TTL);
            UserHolder.saveUser(user);
//            User user1 = UserHolder.getUser();
//            System.out.println("user1:" + user1);
            redisService.del(VERIFY_KEY + loginDTO.getKey());
            return token;
        } catch (AuthenticationException e) {
            log.error("登录失败：{}", e.getMessage());
            throw new AppException("登录失败");
        }
    }

    /**
     * 查询所有用户
     *
     * @param currentPage 当前页数
     * @return 分页结果集
     */
    @Override
    public PageResult showUsers(int currentPage) {
        Page<User> page = new Page<>(currentPage, 10);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("sex", "username", "phone", "email");
        Page<User> selectPage = baseMapper.selectPage(page, wrapper);
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

    /**
     * 通过用户名进行模糊查询
     *
     * @param username    用户名
     * @param currentPage 当前页数
     * @return 分页返回结果集
     */
    @Override
    public PageResult fuzzySearch(String username, int currentPage) {
        Page<User> page = new Page<>(currentPage, 10);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", username).select("sex", "username", "phone", "email");
        Page<User> selectPage = baseMapper.selectPage(page, wrapper);
        PageResult result = null;
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

    /**
     * 修改用户信息
     *
     * @param updateInformationDTO 待修改的用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInformation(UpdateInformationDTO updateInformationDTO) {
        try {
            User user = new User();
            BeanUtils.copyProperties(updateInformationDTO, user);
            User myUser = UserHolder.getUser();
            Long userId = myUser.getId();
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("id", userId);
            baseMapper.update(user, wrapper);
            User newUser = baseMapper.selectOne(wrapper);
            UserHolder.removeUser();
            UserHolder.saveUser(newUser);
            redisService.set(LOGIN_USER_KEY + newUser.getUsername(), newUser);
        } catch (Exception e) {
            throw new AppException("修改失败");
        }
    }

    /**
     * 修改用户性别
     *
     * @param sex 性别
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSex(String sex) {
        try {
            User user = new User();
            if (MALE_SEX.equals(sex)) {
                user.setSex(0);
            } else if (FEMALE_SEX.equals(sex)) {
                user.setSex(1);
            } else {
                throw new AppException("输入信息错误");
            }
            User myUser = UserHolder.getUser();
            Long userId = myUser.getId();
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("id", userId).eq("deleted", 0);
            baseMapper.update(user, wrapper);
            User newUser = baseMapper.selectOne(wrapper);
            UserHolder.removeUser();
            UserHolder.saveUser(newUser);
            redisService.set(LOGIN_USER_KEY + newUser.getUsername(), newUser);
        } catch (Exception e) {
            throw new AppException("修改失败");
        }
    }

    /**
     * 用户忘记密码
     *
     * @param password  用户新设置的密码
     * @param emailCode 邮箱验证码
     * @param username  用户名
     * @param email     用户的邮箱
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forgetPassword(String password, String emailCode, String username, String email) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            User user = baseMapper.selectOne(wrapper);
            if (user == null) {
                throw new AppException("用户不存在，请检查用户名是否正确");
            }
            if (!email.equals(user.getEmail())) {
                throw new AppException("邮箱输入错误");
            }
            try {
                user.setPassword(passwordEncoder.encode(password));
            } catch (Exception e) {
                throw new AppException("密码设置失败");
            }
            String trueEmailCode;
            try {
                trueEmailCode = (String) redisService.get(EMAIL_VERIFY_KEY + email);
            } catch (Exception e) {
                throw new AppException("邮箱验证码失效或出现其他错误");
            }
            if (!trueEmailCode.equals(emailCode)) {
                redisService.del(EMAIL_VERIFY_KEY + email);
                throw new AppException("邮箱验证码错误");
            }
            baseMapper.update(user, wrapper);
        } catch (Exception e) {
            throw new AppException("修改失败");
        }
    }

    /**
     * 用户修改密码
     *
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String newPassword, String oldPassword) {
        User user = UserHolder.getUser();
        try {
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new AppException("旧密码输入错误");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        } catch (Exception e) {
            throw new AppException("重置密码失败");
        }
        baseMapper.updateById(user);
        UserHolder.removeUser();
        redisService.del(LOGIN_USER_KEY + user.getUsername());
    }

    /**
     * 超管重置用户密码
     *
     * @param userId 用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(int userId) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("id", userId);
            User user = baseMapper.selectOne(wrapper);
            if (user == null) {
                throw new AppException("该用户不存在");
            }
            try {
                user.setPassword(passwordEncoder.encode("123456"));
            } catch (Exception e) {
                throw new AppException("设置失败");
            }
            baseMapper.update(user, wrapper);
            UserHolder.removeUser();
            redisService.del(LOGIN_USER_KEY + user.getUsername());
        } catch (Exception e) {
            throw new AppException("重置失败");
        }
    }

    /**
     * 超管修改用户邮箱
     *
     * @param newEmail 新邮箱
     * @param userId   用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String newEmail, int userId) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("id", userId).eq("deleted", 0);
            User user = baseMapper.selectOne(wrapper);
            if (user == null) {
                throw new AppException("该用户不存在");
            }
            user.setEmail(newEmail);
            baseMapper.update(user, wrapper);
            redisService.del(LOGIN_USER_KEY + user.getUsername());
        } catch (Exception e) {
            throw new AppException("修改失败");
        }
    }

    /**
     * 超管删除用户
     *
     * @param userId 用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(int userId) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("id", userId);
            User user = baseMapper.selectOne(wrapper);
            if (user == null) {
                throw new AppException("该用户不存在");
            }
//            user.setDeleted(1);
//            baseMapper.update(user, wrapper);
            baseMapper.deleteById(user.getId());
            redisService.del(LOGIN_USER_KEY + user.getUsername());
        } catch (Exception e) {
            throw new AppException("修改失败");
        }

    }

    @Override
    public User testService() {
        User user = UserHolder.getUser();
        return user;
    }
}
