## demo-mybatis

## DESC:

 - spring 整合 mybatis
 - generator 的使用

---

## mybatis快速上手
#### pom.xml
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.1</version>
    </dependency>

</dependencies>
```
#### application.yml
```yaml
spring:
  datasource:
    name: demo-db
    url: jdbc:mysql://127.0.0.1:3306/javasid
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
#配置mybatis
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.quaint.demo.mybatis
```

---

## generator快速上手:
#### pom.xml
**src下的pom.xml文件**
```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```
```xml
<build>
    <plugins>
        mybatis
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.1</version>
            <configuration>
                <configurationFile>src/main/resources/generator/generatorConfig.xml</configurationFile>
                <overwrite>true</overwrite>
                <verbose>true</verbose>
            </configuration>
        </plugin>
    </plugins>
</build>
```
#### generatorConfig.xml
**根据自己的maven仓库地址修改resources 下的 generator 下的配置文件**
```xml
<generatorConfiguration>
    <!-- 数据库驱动:选择你的本地硬盘上面的数据库驱动包-->
    <classPathEntry  location="E:\.m2\repository\mysql\mysql-connector-java\5.1.6\mysql-connector-java-5.1.6.jar"/>

    <!-- 数据库连接驱动类,URL，用户名、密码   -->
    <jdbcConnection driverClass="com.mysql.jdbc.Driver" connectionURL="jdbc:mysql://localhost:3306/javasid" userId="root" password="root">
        
    <!--等等,详情查看 generator/generatorConfig.xml 标记[ //TODO ] 标签的地方都需要查看是否修改-->
</generatorConfiguration>

```
#### Idea 添加 Edit Configurations 
```text
name: generator
working directory: 选择本项目
command line: mybatis-generator:generate -e
保存,运行即可
```

    