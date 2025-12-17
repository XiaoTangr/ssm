package cn.javat.ssm.service;

import cn.javat.ssm.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 用户注册
     * @param user 用户实体
     * @return 是否注册成功
     */
    boolean register(User user);
    
    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 用户实体，如果登录失败返回null
     */
    User login(String email, String password);
    
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户实体
     */
    User getUserById(Long id);
    
    /**
     * 根据邮箱获取用户信息
     * @param email 用户邮箱
     * @return 用户实体
     */
    User getUserByEmail(String email);
}