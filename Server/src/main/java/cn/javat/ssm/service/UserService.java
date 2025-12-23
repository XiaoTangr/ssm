package cn.javat.ssm.service;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 用户注册
     *
     * @param email    邮箱
     * @param nickname 昵称
     * @param password 密码
     * @return 是否注册成功
     */
    ServiceResult<User> register(String email, String nickname, String password);

    /**
     * 用户登录
     *
     * @param email    邮箱
     * @param password 密码
     * @return 用户实体，如果登录失败返回null
     */
    ServiceResult<User> login(String email, String password);

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户实体
     */
    ServiceResult<User> getUserById(Long id);

    /**
     * 根据邮箱获取用户信息
     *
     * @param email 用户邮箱
     * @return 用户实体
     */
    ServiceResult<User> getUserByEmail(String email);

    ServiceResult<User> getUserByToken(String token);

    ServiceResult<User> resetPassword(String email, String nickName, String password);
}