package cn.javat.ssm.controller;

import cn.javat.ssm.common.ApiResponse;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.UserService;
import cn.javat.ssm.util.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse register(@RequestBody User user) {
        user.setCreateTime(System.currentTimeMillis());
        user.setRole(0); // 默认普通用户
        user.setStatus(0); // 默认正常状态
        user.setIsDeleted(0); // 未删除
        
        boolean result = userService.register(user);
        if (result) {
            user.setPassword(null); // 不返回密码信息
            return ApiResponse.success("注册成功", user);
        } else {
            return ApiResponse.error(400, "邮箱已被注册");
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse login(@RequestBody User loginUser) {
        User user = userService.login(loginUser.getEmail(), loginUser.getPassword());
        if (user != null) {
            user.setPassword(null); // 不返回密码信息
            
            // 生成JWT Token
            String token = jwtUtil.generateToken(user.getId(), user.getEmail());
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userInfo", user);
            
            return ApiResponse.success("登录成功", result);
        } else {
            return ApiResponse.error(400, "邮箱或密码错误");
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public ApiResponse getCurrentUser(HttpServletRequest request) {
        // 从请求中获取用户ID
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                user.setPassword(null); // 不返回密码信息
                return ApiResponse.success(user);
            }
        }
        return ApiResponse.unauthorized("未登录");
    }
}