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
import java.util.regex.Pattern;

/**
 * 登录校验拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    /*    构建如下的放行集合，包含通配符支持
        [{"POST","/users/register"},{"POST","/users/login"},...]

        其中 第一项为请求方法，第二项为请求路径
        请求方法中 * 表示任意方法，请求地址使用正则表达式
     */
    private static final Set<String[]> ALLOW_PATHS = new HashSet<>();

    static {
        // 放行测试：全部方法和全部路径
        ALLOW_PATHS.add(new String[]{"*", "/test/*"});
        // user
        ALLOW_PATHS.add(new String[]{"POST", "/users/register"});
        ALLOW_PATHS.add(new String[]{"POST", "/users/login"});
        ALLOW_PATHS.add(new String[]{"POST", "/users/reset"});
        ALLOW_PATHS.add(new String[]{"GET", "/users/test"});
        // message
        ALLOW_PATHS.add(new String[]{"GET", "/messages"});
        ALLOW_PATHS.add(new String[]{"GET", "/messages/\\d+"});
        ALLOW_PATHS.add(new String[]{"GET", "/messages/\\d+/replies"}); // 查询留言回复
        ALLOW_PATHS.add(new String[]{"GET", "/messages/\\d+/like/status"}); // 查询留言点赞状态
        // admin
        ALLOW_PATHS.add(new String[]{"GET", "/admin/verify"});
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
            // 获取请求URI并去除上下文路径
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();

            // 去除上下文路径，得到相对路径
            String path = requestURI;
            if (contextPath != null && !contextPath.isEmpty() && requestURI.startsWith(contextPath)) {
                path = requestURI.substring(contextPath.length());
            }

            // 根据web.xml中的servlet映射 /api/*，需要去除/api前缀
            if (path.startsWith("/api")) {
                path = path.substring(4); // 去除/api
                if (path.isEmpty()) {
                    path = "/";
                }
            }

            String method = request.getMethod();

            System.out.printf("%s : %s \n", method, path);

            // 检查是否在放行路径集合中
            for (String[] allowPath : ALLOW_PATHS) {
                String allowMethod = allowPath[0];
                String allowPattern = allowPath[1];

                // 检查方法是否匹配（* 表示任意方法）
                boolean methodMatch = allowMethod.equals("*") || allowMethod.equalsIgnoreCase(method);

                // 检查路径是否匹配
                boolean pathMatch = path.equals(allowPattern) ||
                        Pattern.matches(convertToRegex(allowPattern), path);

                if (methodMatch && pathMatch) {
                    System.out.println("路径 " + path + " 已被放行");
                    return true;
                }
            }

            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                // 返回HttpBody错误信息而不是SendError
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(HttpBody.unauthorized().toString());
                return false;
            }
            // 去除Bearer前缀
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
            // 发送服务器错误响应
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(HttpBody.bad().code(500).msg("服务器内部错误").toString());
            return false;
        }
    }

    /**
     * 将路径模式转换为正则表达式
     *
     * @param pattern 路径模式
     * @return 对应的正则表达式
     */
    private String convertToRegex(String pattern) {
        // 将路径中的通配符转换为正则表达式
        String regex = pattern
                .replace("\\", "\\\\")  // 转义反斜杠
                .replace(".", "\\.")
                .replace("*", ".*");    // 将*转换为.*

        return "^" + regex + "$";   // 确保完全匹配
    }
}