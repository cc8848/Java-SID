package com.quaint.demo.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc 配置文件
 * @author qi cong
 * @date 2019-12-25 16:50
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/index").setViewName("index");
      registry.addViewController("/").setViewName("index");
      registry.addViewController("/user").setViewName("user");
      registry.addViewController("/system").setViewName("system");
      registry.addViewController("/login").setViewName("login");
    }

}