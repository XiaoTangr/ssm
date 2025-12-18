package cn.javat.ssm.service.impl;

import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public boolean register(User user) {
        return false;
    }

    @Override
    public User login(String email, String password) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }
}
