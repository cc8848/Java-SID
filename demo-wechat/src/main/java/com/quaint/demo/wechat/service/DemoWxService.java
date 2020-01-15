package com.quaint.demo.wechat.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.quaint.demo.wechat.dto.DemoWxAuthLogin;
import com.quaint.demo.wechat.dto.MaSubscribeMsgDto;
import com.quaint.demo.wechat.dto.MaTemplateMsgDto;
import com.quaint.demo.wechat.dto.MpTemplateMsgDto;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 微信一次订阅消息 服务
 * @author quaint
 * @date 2020-01-07 15:05
 */
public interface DemoWxService {


    /**
     * 用户授权, 获取用户信息
     * @param param 入参
     * @return 微信相关用户信息
     * @throws WxErrorException wx error
     */
    WxMaUserInfo getWxMaUserInfo(DemoWxAuthLogin.Param param) throws WxErrorException;

    /**
     * 用户静默授权登录
     * @param param 入参
     * @return 简要用户信息 openid sessionKey unionid
     * @throws WxErrorException wx error
     */
    WxMaJscode2SessionResult wxDefaultLogin(DemoWxAuthLogin.Param param) throws WxErrorException;

    /**
     * 发送公证号 模板消息
     * @param param 消息dto
     * @return boolean
     * @throws Exception e
     */
    boolean sendMpTempMsg(MpTemplateMsgDto param) throws Exception;

    /**
     * 发送模板消息
     * @param param 消息dto
     * @return boolean
     * @throws Exception e
     */
    @Deprecated
    boolean sendMaTempMsg(MaTemplateMsgDto param) throws Exception;

    /**
     * 发送一次订阅消息
     * @param param 消息内容
     * @return msg
     * @throws Exception e
     */
    String sendMaSubMsg(MaSubscribeMsgDto param) throws Exception;

}
