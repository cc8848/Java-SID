package com.quaint.demo.mail.spi;

import com.quaint.demo.mail.dto.SendMailDto;
import com.quaint.demo.mail.helper.MailHelper;
import com.quaint.demo.mail.utils.MailUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qi cong
 * @date 2019-12-25 19:09
 */
@RestController
@RequestMapping("/demo")
@Api(tags = {"发送邮件"})
public class SendMailSpi {

    @Autowired
    MailHelper mailHelper;

    @PostMapping("/text")
    @Deprecated
    public Boolean sendTextMail(@RequestBody SendMailDto dto){
        MailUtil.sendText(dto.getToMail(),dto.getTitle(),dto.getContent());
        return true;
    }

    @PostMapping("/html")
    @Deprecated
    public Boolean sendHtmlMail(@RequestBody SendMailDto dto){
        try {
            MailUtil.sendHtml(dto.getToMail(),dto.getTitle(),dto.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @PostMapping("/autowired/text")
    public Boolean sendTxtMail(@RequestBody SendMailDto dto){
        mailHelper.sendText(dto.getToMail(),dto.getTitle(),dto.getContent());
        return true;
    }

    @PostMapping("/autowired/html")
    public Boolean sendHtmMail(@RequestBody SendMailDto dto){
        try {
            mailHelper.sendHtml(dto.getToMail(),dto.getTitle(),dto.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
