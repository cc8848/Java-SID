package com.quaint.demo.wechat.service;

import com.quaint.demo.wechat.dto.MaSubscribeMsgDto;
import com.quaint.demo.wechat.dto.MaTemplateMsgDto;
import com.quaint.demo.wechat.dto.MpTemplateMsgDto;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 微信一次订阅消息 服务
 * @author quaint
 * @date 2020-01-07 15:05
 */
public interface DemoWxMsgService {

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
