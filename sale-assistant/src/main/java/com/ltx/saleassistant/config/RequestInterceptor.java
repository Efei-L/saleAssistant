package com.ltx.saleassistant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Configuration
public class RequestInterceptor implements WebMvcConfigurer {
//    @Resource
//    private MyInterceptorOne myInterceptorone;

    @Resource
    private MyInterceptorTwo myInterceptortwo;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(myInterceptorone)
//                .addPathPatterns("/dialog/getChatMessage");
        registry.addInterceptor(myInterceptortwo)
                .addPathPatterns("/jiaju/**")
                .excludePathPatterns("/user/login","/dialog/**");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")    // 虚拟路径
                // file: 表示以本地的路径方式去访问绝对路径。
                .addResourceLocations("file:E:\\java_files\\sale-assistant\\src\\main\\resources\\static\\fengmian_pic\\");    // 绝对路径
    }

}
