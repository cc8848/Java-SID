package com.quaint.demo.mail.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

/**
 * javamail 工具类
 * @author quaint
 * @Date 2019/2/18
 */
@Component
@Slf4j
public class JavaMailUtil {

    /**
     * 静态注入 发送者邮箱
     */
    private static String senderEmail;

    @Value("${spring.mail.username}")
    public void setSenderEmail(String senderEmail){
        JavaMailUtil.senderEmail = senderEmail;
    }


    /***
     * 静态注入 javaMailSender
     */
    private static JavaMailSenderImpl mailSender;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @PostConstruct
    public void init(){
        mailSender = javaMailSender;
    }

    /**
     * 发送包含简单文本的邮件
     * @param toMail 要发送的 邮箱地址
     * @param title  邮件标题
     * @param text   邮件内容
     */
    public static void sendText(String toMail,String title,String text) {
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
    public static void sendHtml(String toMail,String title,String text) throws Exception {
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