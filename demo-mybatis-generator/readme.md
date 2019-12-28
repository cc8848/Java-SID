## mybatis 逆向工程 Demo


使用方法

**先留意自己的maven仓库-> pom文件中的内容**

    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.6</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <!-- mybatis generator 自动生成代码插件 -->
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

    

**根据自己的maven仓库地址修改resources 下的 generator 下的配置文件**

    <!-- 数据库驱动:选择你的本地硬盘上面的数据库驱动包-->
    <classPathEntry  location="E:\.m2\repository\mysql\mysql-connector-java\5.1.6\mysql-connector-java-5.1.6.jar"/>

    <!-- 数据库连接驱动类,URL，用户名、密码   -->
    <jdbcConnection driverClass="com.mysql.jdbc.Driver" connectionURL="jdbc:mysql://localhost:3306/javasid" userId="root" password="root">
    
    等等 标记//TODO 标签的地方都需要查看是否修改

**在添加一个maven运行代码**

    name: generator
    working directory: 选择本项目
    command line: mybatis-generator:generate -e
    保存,运行即可