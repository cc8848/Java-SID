## demo-mybatis-plus

整合了mybatis-plus 和 swagger
    
**pom.xml**
    
    <dependencies>
        <!-- swagger 由父工程pom 提供 -->
        <!-- mysql -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- druid数据库连接池  -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>${druid.version}</version>
        </dependency>

        <!-- mybatis-plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
    </dependencies>
    
**application.yml**

    spring:
      application:
        name: demo-mybatis-plus
      datasource:
        name: demo-db
        url: jdbc:mysql://localhost:3306/javasid?setUnicode=true&characterEncoding=UTF-8&useSSL=false
        username: root
        password: root
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.alibaba.druid.pool.DruidDataSource
    mybatis-plus:
      mapper-locations: classpath:/mapper/*Mapper.xml
      type-aliases-package: com.quaint.demo.mybatis.plus.po
      configuration:
        # 驼峰下划线转换
        map-underscore-to-camel-case: true
        log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
        
        
**代码生成器官网配置**

    https://mp.baomidou.com/config/generator-config.html