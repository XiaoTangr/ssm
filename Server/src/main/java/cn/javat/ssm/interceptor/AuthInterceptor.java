package cn.javat.ssm.interceptor;

import cn.javat.ssm.common.HttpBody;
import cn.javat.ssm.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 登录校验拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    // 免登录路径集合
    private static final Set<String> EXCLUDE_PATHS = new HashSet<>();
    
    // 免登录路径前缀集合
    private static final Set<String> EXCLUDE_PREFIXES = new HashSet<>();
    
    // 免登录的正则表达式路径模式
    private static final Set<String> EXCLUDE_PATTERNS = new HashSet<>();

    static {
        // 完全匹配的免登录路径
        EXCLUDE_PATHS.add("/api/users/register");
        EXCLUDE_PATHS.add("/api/users/login");
        EXCLUDE_PATHS.add("/api/messages");
        EXCLUDE_PATHS.add("/api/users/reset");
        
        // 前缀匹配的免登录路径
        EXCLUDE_PREFIXES.add("/api/messages/");
        
        // 正则表达式匹配的免登录路径
        EXCLUDE_PATTERNS.add("/api/messages/\\d+/replies"); // 查询留言回复
    }

    @Autowired
    public AuthInterceptor(JwtUtil jwtUtil) {
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        try {
            String path = request.getServletPath();
            String method = request.getMethod();
            
            // 检查是否在免登录路径集合中
            if (EXCLUDE_PATHS.contains(path)) {
                return true;
            }
            
            // 检查是否以免登录路径前缀开头
            for (String prefix : EXCLUDE_PREFIXES) {
                if (path.startsWith(prefix)) {
                    // GET方法且不是需要登录的接口
                    if ("GET".equals(method) && 
                        !path.endsWith("/like") && 
                        !path.endsWith("/like/status") &&
                        !path.endsWith("/status")) {
                        return true;
                    }
                    // 特殊处理：/api/messages/{messageId} 的GET方法
                    if ("GET".equals(method) && path.matches("/api/messages/\\d+$")) {
                        return true;
                    }
                }
            }
            
            // 检查是否匹配免登录的正则表达式模式
            for (String pattern : EXCLUDE_PATTERNS) {
                if (path.matches(pattern)) {
                    return true;
                }
            }
            
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
//                返回HttpBody错误信息而不是SendError
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(HttpBody.unauthorized().toString());
                return false;
            }
//            去除Bearer前缀
            token = token.replace("Bearer ", "");
            Claims claims = jwtUtil.validateToken(token);
            if (claims == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(HttpBody.bad().code(401).msg("token验证失败").toString());
                return false;
            }
            response.setHeader("Authorization", "Bearer " + jwtUtil.refreshToken(token));
            return true;
        } catch (Exception e) {
//            发送服务器错误响应
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(HttpBody.bad().code(500).msg("服务器内部错误").toString());
            return false;
        }
    }

}