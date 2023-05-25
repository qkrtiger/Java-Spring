package com.raspberry.board.config;

import com.raspberry.board.util.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    SessionInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/css/**","/js/**","/images/**")
                .excludePathPatterns("/joinForm","/idCheck","/joinProc")
                .excludePathPatterns("/loginForm","/loginProc","/error/**");
    }
}
