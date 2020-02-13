package com.quaint.demo.easy.excel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

/**
 * @author quaint
 * @date 2020-01-14 10:57
 */
@SpringBootApplication
public class EasyExcelApplication {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //指定文件大小
        factory.setMaxFileSize("50MB");
        /// 设定上传文件大小
        factory.setMaxRequestSize("100MB");
        return factory.createMultipartConfig();
    }

    public static void main(String[] args) {
        SpringApplication.run(EasyExcelApplication.class,args);
    }

}
