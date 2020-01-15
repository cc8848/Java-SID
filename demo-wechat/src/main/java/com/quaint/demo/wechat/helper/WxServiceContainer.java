package com.quaint.demo.wechat.helper;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.quaint.demo.wechat.enums.WxTypeEnum;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 微信服务 容器
 *
 * Demo 无需使用, 多渠道解决方案
 * 快速上手:
 *
 * Service 中 注入 微信服务 容器
 * @Autowired
 * WxServiceContainer wxServiceContainer;
 *
 * 根据不同类型,获取不同的 service
 * @see WxTypeEnum
 * WxMpService wxMpService = wxServiceContainer.getWxMpService(param.getChannel()).orElseThrow(Exception::new);
 * WxMaService wxMaService = wxServiceContainer.getWxMaService(param.getChannel()).orElseThrow(Exception::new);
 *
 * @author quaint
 * @date 2020-01-06 11:44
 */
@Component
@Deprecated
public class WxServiceContainer {

    /**
     * 公众号 service
     */
    private Map<Integer, WxMpService> wxMpServiceMap;

    /**
     * 小程序 service
     */
    private Map<Integer, WxMaService> wxMaServiceMap;


    /**
     * 注册公众号服务
     * @param channel 配置的标识
     * @param wxMpService 服务
     */
    public void registerMpService(Integer channel, WxMpService wxMpService){
        if (Objects.isNull(wxMpServiceMap)){
            wxMpServiceMap = new HashMap<>(4);
        }
        wxMpServiceMap.put(channel,wxMpService);
    }

    /**
     * 注册小程序服务
     * @param channel 配置的标识
     * @param wxMaService 服务
     */
    public void registerMaService(Integer channel, WxMaService wxMaService){
        if(Objects.isNull(wxMaServiceMap)){
            wxMaServiceMap = new HashMap<>(4);
        }
        wxMaServiceMap.put(channel,wxMaService);
    }


    /**
     * 根据channel获取小程序服务
     * @param channel channel
     * @return service
     */
    public Optional<WxMaService> getWxMaService(Integer channel){
        if(wxMaServiceMap != null){
            return Optional.ofNullable(wxMaServiceMap.get(channel));
        }
        return Optional.empty();
    }

    /**
     * 根据channel获取公众号服务
     * @param channel channel
     * @return service
     */
    public Optional<WxMpService> getWxMpService(Integer channel){
        if(wxMpServiceMap != null){
            return Optional.ofNullable(wxMpServiceMap.get(channel));
        }
        return Optional.empty();
    }


}
