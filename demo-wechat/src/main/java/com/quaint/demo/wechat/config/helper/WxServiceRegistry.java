package com.quaint.demo.wechat.config.helper;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quaint.demo.wechat.enums.WxTypeEnum;
import com.quaint.demo.wechat.mapper.DemoWxChannelMapper;
import com.quaint.demo.wechat.po.DemoWxChannelPO;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 微信服务注册, 从数据库查询出 配置信息
 * @author quaint
 * @date 2020-01-06 13:49
 */
@Configuration
public class WxServiceRegistry implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    WxServiceContainer wxServiceContainer;

    @Autowired
    DemoWxChannelMapper demoWxChannelMapper;

    private AtomicBoolean hasSet = new AtomicBoolean(Boolean.FALSE);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if(!hasSet.get()) {

            DemoWxChannelPO queryPo = new DemoWxChannelPO();
            queryPo.setValid(Boolean.TRUE);
            QueryWrapper<DemoWxChannelPO> queryWrapper = new QueryWrapper<>(queryPo);
            // 查询出所有的配置信息
            List<DemoWxChannelPO> demoWxChannelPoList = demoWxChannelMapper.selectList(queryWrapper);
            demoWxChannelPoList.forEach(wxChannelPo -> {
                WxTypeEnum type = WxTypeEnum.getEnumByType(wxChannelPo.getType());
                switch (type){
                    //公众号
                    case WX_TYPE_MP:
                        registerMpService(wxChannelPo);
                        break;
                    //小程序
                    case WX_TYPE_MA:
                        registerMaService(wxChannelPo);
                        break;
                    default:
                        break;
                }
            });
        }

    }


    /**
     * 注册公众号服务
     * @param demoWxPo
     */
    private void registerMpService(DemoWxChannelPO demoWxPo){


        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(demoWxPo.getAppId());
        configStorage.setSecret(demoWxPo.getAppSecret());
        configStorage.setToken(demoWxPo.getToken());
        configStorage.setAesKey(demoWxPo.getAesKey());

        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);

        wxServiceContainer.registerMpService(demoWxPo.getChannel(),wxMpService);
    }

    /**
     * 注册小程序服务
     * @param demoWxPo po
     */
    private void registerMaService(DemoWxChannelPO demoWxPo){

        WxMaInMemoryConfig maInMemoryConfig = new WxMaInMemoryConfig();
        maInMemoryConfig.setAppid(demoWxPo.getAppId());
        maInMemoryConfig.setSecret(demoWxPo.getAppSecret());

        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(maInMemoryConfig);

        wxServiceContainer.registerMaService(demoWxPo.getChannel(),wxMaService);
    }


}
