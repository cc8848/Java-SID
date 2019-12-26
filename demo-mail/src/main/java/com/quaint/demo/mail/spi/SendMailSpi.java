package com.quaint.demo.mail.spi;

import com.quaint.demo.mail.dto.SendMailDto;
import com.quaint.demo.mail.utils.JavaMailUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qi cong
 * @date 2019-12-25 19:09
 */
@RestController
@RequestMapping("demo")
@Api(tags = {"发送邮件"})
public class SendMailSpi {


    @PostMapping("text")
    public Boolean sendTextMail(@RequestBody SendMailDto dto){
        JavaMailUtil.sendText(dto.getToMail(),dto.getTitle(),dto.getContent());
        return true;
    }

    @PostMapping("html")
    public Boolean sendHtmlMail(@RequestBody SendMailDto dto){
        try {
            JavaMailUtil.sendHtml(dto.getToMail(),dto.getTitle(),dto.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
