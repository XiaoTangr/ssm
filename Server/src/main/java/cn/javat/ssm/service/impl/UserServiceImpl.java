package cn.javat.ssm.service.impl;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.mapper.UserMapper;
import cn.javat.ssm.service.UserService;
import cn.javat.ssm.util.JwtUtil;
import cn.javat.ssm.util.PasswordEncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncryptUtil passwordEncryptUtil;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil, PasswordEncryptUtil passwordEncryptUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncryptUtil = passwordEncryptUtil;
    }


    /**
     * 注册
     *
     * @param email    邮箱
     * @param nickname 昵称
     * @param password 密码
     * @return User 成功 -1 邮箱已存在 -2 注册失败
     */
    @Override
    public ServiceResult<User> register(String email, String nickname, String password) {

        User user = userMapper.selectByEmail(email);
        if (user != null) {
            return ServiceResult.error(-1);
        }
        password = passwordEncryptUtil.encryptPassword(password);
        user = new User();
        user.setEmail(email);
        user.setNickname(nickname);
        user.setPassword(password);
        user.setRole(0);
        user.setStatus(0);
        user.setIsDeleted(0);
        user.setCreateTime(System.currentTimeMillis());
        if (userMapper.insert(user) > 0) {
            return ServiceResult.ok(user);
        }
        return ServiceResult.error(-2);
    }

    @Override
    public ServiceResult<User> login(String email, String password) {
        User user = userMapper.selectByEmail(email);

        if (user == null) {
            return ServiceResult.error(-2);
        }

        String dbPassword = user.getPassword();
        if (!passwordEncryptUtil.verifyPassword(password, dbPassword)) {
            return ServiceResult.error(-1);
        }
        return ServiceResult.ok(user);
    }

    @Override
    public ServiceResult<User> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            return ServiceResult.ok(user);
        }
        return ServiceResult.error(404);
    }

    @Override
    public ServiceResult<User> getUserByEmail(String email) {
        User user = userMapper.selectByEmail(email);
        if (user != null) {
            return ServiceResult.ok(user);
        }
        return ServiceResult.error(404);
    }


    /**
     * 通过token获取用户信息
     *
     * @param token 令牌
     * @return User 获取成功 -1 令牌无效 -2 令牌不存在
     */
    @Override
    public ServiceResult<User> getUserByToken(String token) {
        ServiceResult<User> result;
        if (token == null) {
            result = ServiceResult.error(-2);
        } else {
            if (token.startsWith("Bearer ")) {
                token = token.replace("Bearer ", "");
            }
            if (jwtUtil.validateToken(token) == null) {
                result = ServiceResult.error(-1);
            } else {
                Long userId = jwtUtil.getUserIdFromToken(token);
                if (userId != null) {
                    User user = userMapper.selectById(userId);
                    if (user != null) {
                        result = ServiceResult.ok(user);
                    } else {
                        result = ServiceResult.error(-1);
                    }
                } else {
                    result = ServiceResult.error(-2);
                }
            }
        }
        return result;
    }
}