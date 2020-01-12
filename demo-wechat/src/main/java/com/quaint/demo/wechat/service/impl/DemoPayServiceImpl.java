package com.quaint.demo.wechat.service.impl;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.quaint.demo.wechat.config.WxProperties;
import com.quaint.demo.wechat.dto.Prepay;
import com.quaint.demo.wechat.dto.Refund;
import com.quaint.demo.wechat.service.DemoPayService;
import com.quaint.demo.wechat.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author quaint
 * @date 2020-01-12 15:57
 */
@Service
@EnableConfigurationProperties(WxProperties.class)
@Slf4j
public class DemoPayServiceImpl implements DemoPayService, InitializingBean {

    @Autowired
    WxProperties wxProperties;

    private WxPayService wxPayService;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public Prepay.Response prepay(Prepay.Request request) {

        log.info("获取历史待支付订单( db 查询)");
        String historyOrder = "demoPayOrderMapper.select();";

        if (historyOrder.equals(request.getOrderNo())){
            log.info("有历史待支付单 直接使用");
        } else{
            log.info("没有历史订单创建支付订单 并保存到数据库 demoPayOrderMapper.insert(demoPayOrderPO);");
        }

        // 准备预支付单请求报文
        WxPayUnifiedOrderRequest wxReq = new WxPayUnifiedOrderRequest();
        // 商品简单描述
        wxReq.setBody("娃哈哈");
        // 商户订单号
        wxReq.setOutTradeNo("DEMO0001");
        // 金额 分为单位
        wxReq.setTotalFee(1);
        // 交易类型
        wxReq.setTradeType("JSAPI");
        // 用户openid
        wxReq.setOpenid(request.getUserId());
        // 回调地址
        wxReq.setNotifyUrl(wxProperties.getPayProperties().getNotifyUrl());
        // ip 地址
        wxReq.setSpbillCreateIp(IpUtils.getMachineIP());
        // 设置附件 db , 如订单id
        wxReq.setAttach("1");

        log.info("准备记录支付日志到 db  ->  new demoPayLogPO();");

        try {
            // 下预支付单
            WxPayMpOrderResult wxPayMpOrderResult = this.wxPayService.createOrder(wxReq);

            log.info("记录响应信息 demoPayLogPO.setResponseBodyLog(JSONObject.toJSONString(wxPayMpOrderResult));");

            // 更新支付的信息
            log.info("demoPayOrderPO.setStatus(待支付);");
            log.info("demoPayOrderPO.setPrepayid(wxPayMpOrderResult.getPackageValue().split(=)[1]);");
            log.info("demoPayOrderPO.setPrepayResult(wxPayLogPO.getResponseBodyLog());");
            // 有效期为二小时
            log.info("demoPayOrderPO.setExpirationTime(LocalDateTime.now().plusHours(2L));");
            log.info("demoPayOrderPO.updateById(wxPayOrderPO);");

            // 设置结果报文
            return Prepay.Response.builder()
                    .prepayId("demoPayOrderPO.getPrepayid()")
                    .data(wxPayMpOrderResult).build();

        }catch (WxPayException e){
            log.error(e.getMessage(),e);
            log.info("demoPayLogPO.setResponseBodyLog(e.getXmlString());");
            return null;
        }finally {
            // 保存日志
            log.info("完成后,记录支付日志到 db  ->  demoPayMapper.insert");
        }
    }


    @Override
    public Refund.Response refund(Refund.Request request) {
        return null;
    }

    @Override
    public void afterPropertiesSet() {

        WxPayConfig payConfig = new WxPayConfig();
        WxProperties.PayProperties payProperties = this.wxProperties.getPayProperties();
        payConfig.setAppId(StringUtils.trimToNull(payProperties.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(payProperties.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(payProperties.getMchKey()));
        payConfig.setKeyPath(StringUtils.trimToNull(payProperties.getKeyPath()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
    }

}
