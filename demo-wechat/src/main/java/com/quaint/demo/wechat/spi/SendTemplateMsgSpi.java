package com.quaint.demo.wechat.spi;

import com.quaint.demo.wechat.dto.MaSubscribeMsgDto;
import com.quaint.demo.wechat.dto.MaTemplateMsgDto;
import com.quaint.demo.wechat.dto.MpTemplateMsgDto;
import com.quaint.demo.wechat.service.DemoWxService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quaint
 * @date 2020-01-06 12:02
 */
@Api(tags = {"发送模板消息"})
@RestController
@RequestMapping("/send")
public class SendTemplateMsgSpi {

    @Autowired
    DemoWxService demoWxService;

    @PostMapping("/mp/msg")
    public boolean sendMpTempMsg(@RequestBody MpTemplateMsgDto param) throws Exception{
        return demoWxService.sendMpTempMsg(param);
    }

    @PostMapping("/ma/msg")
    @Deprecated
    public boolean sendMaTempMsg(@RequestBody MaTemplateMsgDto param) throws Exception{
        return demoWxService.sendMaTempMsg(param);
    }

    @PostMapping("/ma/sub/msg")
    public String sendMaSubMsg(@RequestBody MaSubscribeMsgDto param) throws Exception{
        return demoWxService.sendMaSubMsg(param);
    }


}
