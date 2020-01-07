package com.quaint.demo.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author quaint
 * @date 2020-01-06 10:35
 */
@SpringBootApplication
@MapperScan("com.quaint.demo.wechat.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
