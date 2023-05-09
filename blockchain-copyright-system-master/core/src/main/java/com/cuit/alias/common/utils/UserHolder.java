package com.cuit.alias.common.utils;


import com.cuit.alias.entity.User;

/**
 * @author lisihan
 * @Description ThreadLocal
 * @date 2023/1/17-15:27
 */
public class UserHolder {
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void saveUser(User user){
        threadLocal.set(user);
    }

    public static User getUser(){
        return threadLocal.get();
    }

    public static void removeUser(){
        threadLocal.remove();
    }
}
