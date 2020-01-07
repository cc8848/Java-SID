package com.quaint.demo.wechat.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import com.google.gson.GsonBuilder;
import com.quaint.demo.wechat.config.helper.JsonExclusionStrategy;
import com.quaint.demo.wechat.config.helper.WxServiceContainer;
import com.quaint.demo.wechat.constant.WxUrlConstant;
import com.quaint.demo.wechat.dto.MaSubscribeMsgDto;
import com.quaint.demo.wechat.dto.MaTemplateMsgDto;
import com.quaint.demo.wechat.dto.MpTemplateMsgDto;
import com.quaint.demo.wechat.service.WxMsgService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author quaint
 * @date 2020-01-07 15:06
 */
@Service
@Slf4j
public class WxMsgServiceImpl implements WxMsgService {

    // 设置json 排除策略
//    private static ExclusionStrategy myExclusionStrategy = new ExclusionStrategy(){
//        @Override
//        public boolean shouldSkipField(FieldAttributes f) {
//            return f.getName().equals("channel");
//        }
//
//        @Override
//        public boolean shouldSkipClass(Class<?> clazz) {
//            return false;
//        }
//    };
//    private static GsonBuilder gsonBuilder = new GsonBuilder();
//    static {
//        gsonBuilder.setExclusionStrategies(myExclusionStrategy);
//    }

    @Autowired
    WxServiceContainer wxServiceContainer;

    @Override
    public boolean sendMpTempMsg(MpTemplateMsgDto param) throws Exception {
        WxMpService wxMpService = wxServiceContainer.getWxMpService(param.getChannel()).orElseThrow(Exception::new);

        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setTemplateId(param.getTemplateId());
        wxMpTemplateMessage.setToUser(param.getOpenId());
        wxMpTemplateMessage.setUrl(param.getUrl());
        wxMpTemplateMessage.setData(param.getData());
        try {
            // 发送模板消息
            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean sendMaTempMsg(MaTemplateMsgDto param) throws Exception {
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
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String sendMaSubMsg(MaSubscribeMsgDto param) throws Exception{
        // 获取小程序对应 服务
        WxMaService wxMaService = wxServiceContainer.getWxMaService(param.getChannel()).orElseThrow(Exception::new);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new JsonExclusionStrategy(Collections.singletonList("channel")));
        String returnJson = wxMaService.post(WxUrlConstant.MA_SUBSCRIBE_MSG_URL,gsonBuilder.create().toJson(param));
        log.info("wx return json is :[{}]",returnJson);
        return returnJson;
    }
}
