package com.quaint.demo.wechat.spi;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import com.quaint.demo.wechat.config.helper.WxServiceContainer;
import com.quaint.demo.wechat.dto.MaTemplateMsgDto;
import com.quaint.demo.wechat.dto.MpTemplateMsgDto;
import io.swagger.annotations.Api;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
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
    WxServiceContainer wxServiceContainer;

    @PostMapping("/mp/msg")
    public void sendMpTempMsg(@RequestBody MpTemplateMsgDto param) throws Exception{

        WxMpService wxMpService = wxServiceContainer.getWxMpService(param.getChannel()).orElseThrow(Exception::new);

        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setTemplateId(param.getTemplateId());
        wxMpTemplateMessage.setToUser(param.getOpenId());
        wxMpTemplateMessage.setUrl(param.getUrl());
        wxMpTemplateMessage.setData(param.getData());
        try {
            // 发送模板消息
            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/ma/msg")
    public void sendMaTempMsg(@RequestBody MaTemplateMsgDto param) throws Exception{

        WxMaService wxMaService = wxServiceContainer.getWxMaService(param.getChannel()).orElseThrow(Exception::new);

        WxMaTemplateMessage wxMaTemplateMessage = new WxMaTemplateMessage();
        wxMaTemplateMessage.setFormId(param.getFormId());
        wxMaTemplateMessage.setTemplateId(param.getTemplateId());
        wxMaTemplateMessage.setToUser(param.getOpenId());
        wxMaTemplateMessage.setPage(param.getPage());
        wxMaTemplateMessage.setData(param.getData());
        try {
            // 发送模板消息
            wxMaService.getMsgService().sendTemplateMsg(wxMaTemplateMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
