package com.quaint.demo.wechat.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quaint.demo.wechat.config.WxProperties;
import com.quaint.demo.wechat.constant.WxUrlConstant;
import com.quaint.demo.wechat.dto.DemoWxAuthLogin;
import com.quaint.demo.wechat.dto.MaSubscribeMsgDto;
import com.quaint.demo.wechat.dto.MaTemplateMsgDto;
import com.quaint.demo.wechat.dto.MpTemplateMsgDto;
import com.quaint.demo.wechat.helper.JsonExclusionStrategy;
import com.quaint.demo.wechat.service.DemoWxService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @author quaint
 * @date 2020-01-07 15:06
 */
@Service
@Slf4j
@EnableConfigurationProperties(WxProperties.class)
public class DemoWxServiceImpl implements DemoWxService {

    @Autowired
    private WxProperties wxProperties;

    private WxMaService wxMaService;

    private WxMpService wxMpService;

    /**
     * 注册服务
     */
    @PostConstruct
    void init(){

        // 小程序服务
        WxMaInMemoryConfig maInMemoryConfig = new WxMaInMemoryConfig();
        maInMemoryConfig.setAppid(wxProperties.getMiniApp().getAppId());
        maInMemoryConfig.setSecret(wxProperties.getMiniApp().getAppSecret());
        wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(maInMemoryConfig);

        // 公众号服务
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(wxProperties.getMa().getAppId());
        configStorage.setSecret(wxProperties.getMa().getAppSecret());
        configStorage.setToken(wxProperties.getMa().getToken());
        configStorage.setAesKey(wxProperties.getMa().getAesKey());
        wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
    }


    @Override
    public WxMaUserInfo getWxMaUserInfo(DemoWxAuthLogin.Param param) throws WxErrorException {
        WxMaUserService userService = wxMaService.getUserService();
        WxMaJscode2SessionResult sessionInfo = userService.getSessionInfo(param.getCode());
        return userService.getUserInfo(sessionInfo.getSessionKey(), param.getEncryptedData(), param.getIvStr());
    }

    @Override
    public WxMaJscode2SessionResult wxDefaultLogin(DemoWxAuthLogin.Param param) throws WxErrorException {
        WxMaUserService userService = wxMaService.getUserService();
        return userService.getSessionInfo(param.getCode());
    }

    @Override
    public boolean sendMpTempMsg(MpTemplateMsgDto param) {

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
    public boolean sendMaTempMsg(MaTemplateMsgDto param) {

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
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setExclusionStrategies(new JsonExclusionStrategy(Collections.singletonList("channel"))).create();
        // 发送模板消息的时候过滤掉 channel, demo 可以忽略 所有与channel 相关代码
        log.info("json 过滤策略 生成的 json 对象:[{}]",gson.toJson(param));
        String returnJson = wxMaService.post(WxUrlConstant.MA_SUBSCRIBE_MSG_URL,gson.toJson(param));
        log.info("wx return json is :[{}]",returnJson);
        return returnJson;
    }
}
