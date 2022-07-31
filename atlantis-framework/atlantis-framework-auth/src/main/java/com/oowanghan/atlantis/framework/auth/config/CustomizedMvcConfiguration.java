package com.oowanghan.atlantis.framework.auth.config;

import com.oowanghan.atlantis.framework.auth.JwtAuthFilter;
import com.oowanghan.atlantis.framework.auth.JwtAuthInterceptor;
import com.oowanghan.atlantis.framework.auth.JwtTokenProcesser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(JwtAuthAutoConfiguration.class)
public class CustomizedMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtAuthInterceptor jwtAuthInterceptor;
    @Autowired
    private JwtTokenProcesser jwtTokenProcesser;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> registration() {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtTokenProcesser);
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>(jwtAuthFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
