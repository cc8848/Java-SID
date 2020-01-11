# demo-mail

## DESC: 

 - spring integration mail

## 快速上手:

#### pom.xml

```xml
<dependencies>
    <!-- swagger spring 由父工程 pom 提供 -->
    <!-- java mail -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
</dependencies>
```
#### application.yml

```yaml
spring: 
    mail:
        # host: smtp.exmail.qq.com  #企业
        host: smtp.qq.com  # 个人
        # 端口号465或587
        port: 587
        # 你的邮箱 或者 企业邮箱
        username: you_email@qq.com
        # 这里QQ邮箱开通POP3/SMTP提供的授权码，如果邮箱服务商没有授权码，可以使用密码代替
        password: pwd_or_code
        # 下面默认就好
        protocol: smtp
        default-encoding: UTF-8
```

#### 编写MailHelper

com.quaint.demo.mail.helper.MailHelper
```java
@Component
public class MailHelper {

    /**
     * 注入 JavaMailSender
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 注入配置文件中的 username 用来当发送邮件的邮箱
     */
    @Value("${spring.mail.username}")
    private String senderEmail;


    // ======= function test ======= 

    /**
     * 发送包含简单文本的邮件
     * @param toMail 要发送的 邮箱地址
     * @param title  邮件标题
     * @param text   邮件内容
     */
    public void sendText(String toMail,String title,String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置收件人
        simpleMailMessage.setTo(new String[] {toMail});
        //寄件人
        simpleMailMessage.setFrom(senderEmail);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(text);
        // 发送邮件
        mailSender.send(simpleMailMessage);
        log.info("JavaMailUtil类提示:---邮件已发送---");
    }

    /**
     * 发送包含简单html文本的邮件
     * @param toMail 要发送的 邮箱地址
     * @param title  邮件标题
     * @param text   邮件内容
     * @throws Exception ex
     */
    public void sendHtml(String toMail,String title,String text) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        //发送到传进来的邮箱
        mimeMessageHelper.setTo(toMail);
        mimeMessageHelper.setFrom(senderEmail);
        mimeMessageHelper.setSubject(title);
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head><body>");
        sb.append(text);
        sb.append("</body></html>");
        // 启用html
        mimeMessageHelper.setText(sb.toString(), true);
        // 发送邮件
        mailSender.send(mimeMessage);

        log.info("邮件已发送");
    }
}
```


