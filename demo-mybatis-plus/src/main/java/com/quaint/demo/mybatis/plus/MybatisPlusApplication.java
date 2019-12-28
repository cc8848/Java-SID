package com.quaint.demo.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qi cong
 * @date 2019-12-24 15:09
 */
@SpringBootApplication
@MapperScan(basePackages = "com.quaint.demo.mybatis.plus.mapper")
public class MybatisPlusApplication {

    public static void main(String[] args) {

        SpringApplication.run(MybatisPlusApplication.class,args);

    }

}
