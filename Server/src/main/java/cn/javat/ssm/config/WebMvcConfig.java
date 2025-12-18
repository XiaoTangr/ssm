package cn.javat.ssm.config;

import cn.javat.ssm.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置类，注册拦截器
 */
@Configuration // 标记为配置类，让Spring扫描到
public class WebMvcConfig implements WebMvcConfigurer {


    private final LoginInterceptor loginInterceptor;

    @Autowired
    public WebMvcConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    /**
     * 注册拦截器，并配置拦截/放行规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**"); // 拦截所有以/api开头的请求
    }
}