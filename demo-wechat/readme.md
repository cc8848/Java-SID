## demo-wechat

## DESC:

 - spring 整合 swagger


## 快速上手(未整理好,现在项目有点乱,微信暂时不推荐参考):


#### pom.xml:
    
```xml
<dependencies>
    <!-- ConfigurationProperties 依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- 引入微信 公众号 依赖 -->
    <dependency>
        <groupId>com.github.binarywang</groupId>
        <artifactId>weixin-java-mp</artifactId>
        <version>${weixin.version}</version>
    </dependency>
    <!-- 引入微信 小程序 依赖 -->
    <dependency>
        <groupId>com.github.binarywang</groupId>
        <artifactId>weixin-java-miniapp</artifactId>
        <version>${weixin.version}</version>
    </dependency>
    <!-- 引入微信 支付 依赖 -->
    <dependency>
        <groupId>com.github.binarywang</groupId>
        <artifactId>weixin-java-pay</artifactId>
        <version>${weixin.version}</version>
    </dependency>
    <!-- 引入微信 开放平台 依赖 -->
    <dependency>
        <groupId>com.github.binarywang</groupId>
        <artifactId>weixin-java-open</artifactId>
        <version>${weixin.version}</version>
    </dependency>
</dependencies>
```

    
#### application.yml:

```yaml
wx:
  # (暂未整合)
  pay:
    # appid
    appId: appid
    # 微信支付商户号
    mchId: mchId
    #微信支付商户密钥
    mchKey: mchKey
    # 服务商模式下的子商户公众账号ID
    # subAppId:
    # 服务商模式下的子商户号
    # subMchId:
    # p12证书的位置，可以指定绝对路径，也可以指定类路径（以classpath:开头）
    keyPath: classpath:key/key.p12
```
