package cn.javat.ssm.interceptor;

import cn.javat.ssm.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashSet;
import java.util.Set;

/**
 * 登录校验拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    // 免登录路径集合
    private static final Set<String> EXCLUDE_PATHS = new HashSet<>();

    static {
        EXCLUDE_PATHS.add("/api/users/test");
        EXCLUDE_PATHS.add("/api/users/register");
        EXCLUDE_PATHS.add("/api/users/login");
    }

    @Autowired
    public LoginInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    /**
     * 请求到达控制器之前执行（核心方法，常用）
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return true：放行，继续执行控制器方法；false：拦截，不再向下执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String path = request.getServletPath();
            if (EXCLUDE_PATHS.contains(path)) {
                return true;
            }
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "缺少Token");
                return false;
            }
            Claims claims = jwtUtil.validateToken(token);
            if (claims == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token验证失败");
                return false;
            }
            response.setHeader("Authorization", jwtUtil.refreshToken(token));
            return true;

        } catch (Exception e) {
//            发送服务器错误响应
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器错误");
            return false;
        }
    }

}