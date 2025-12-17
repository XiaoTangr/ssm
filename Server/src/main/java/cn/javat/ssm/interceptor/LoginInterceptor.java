package cn.javat.ssm.interceptor;

import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.UserService;
import cn.javat.ssm.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                             HttpServletResponse response, 
                             Object handler) throws Exception {
        // 获取请求的URI
        String uri = request.getRequestURI();
        
        // 放行公开接口
        if (uri.startsWith("/api/users/login") || 
            uri.startsWith("/api/users/register") || 
            uri.startsWith("/api/messages")) {
            return true;
        }
        
        // 从请求头中获取JWT Token
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // 去掉"Bearer "前缀
            
            try {
                // 验证Token并获取用户信息
                Claims claims = jwtUtil.validateToken(token);
                Long userId = jwtUtil.getUserIdFromToken(token);
                String email = jwtUtil.getEmailFromToken(token);
                
                // 可以将用户信息放入request中供后续使用
                request.setAttribute("userId", userId);
                request.setAttribute("email", email);
                
                return true;
            } catch (Exception e) {
                // Token无效
                response.setStatus(401);
                return false;
            }
        }
        
        // 没有Token或Token无效
        response.setStatus(401);
        return false;
    }
}