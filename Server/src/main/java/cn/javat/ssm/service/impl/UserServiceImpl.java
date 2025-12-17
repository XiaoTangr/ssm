package cn.javat.ssm.service.impl;

import cn.javat.ssm.mapper.UserMapper;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.UserService;
import cn.javat.ssm.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public boolean register(User user) {
        // 检查邮箱是否已存在
        User existingUser = userMapper.selectByEmail(user.getEmail());
        if (existingUser != null) {
            return false;
        }
        
        // 密码加密
        user.setPassword(PasswordEncoder.encode(user.getPassword()));
        
        // 插入新用户
        int result = userMapper.insert(user);
        return result > 0;
    }
    
    @Override
    public User login(String email, String password) {
        User user = userMapper.selectByEmail(email);
        if (user != null && PasswordEncoder.matches(password, user.getPassword()) && user.getIsDeleted() == 0) {
            return user;
        }
        return null;
    }
    
    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
    
    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }
}