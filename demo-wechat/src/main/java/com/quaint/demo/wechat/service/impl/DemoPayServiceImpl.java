package com.quaint.demo.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.quaint.demo.wechat.config.WxProperties;
import com.quaint.demo.wechat.dto.Prepay;
import com.quaint.demo.wechat.dto.Refund;
import com.quaint.demo.wechat.enums.pay.DirectionEnum;
import com.quaint.demo.wechat.enums.pay.PayOrderStatusEnum;
import com.quaint.demo.wechat.enums.pay.SourceTypeEnum;
import com.quaint.demo.wechat.enums.pay.WxRefundOrderStatusEnum;
import com.quaint.demo.wechat.mapper.DemoWxLogMapper;
import com.quaint.demo.wechat.mapper.DemoWxPayOrderMapper;
import com.quaint.demo.wechat.mapper.DemoWxRefundOrderMapper;
import com.quaint.demo.wechat.po.DemoWxLogPO;
import com.quaint.demo.wechat.po.DemoWxPayOrderPO;
import com.quaint.demo.wechat.po.DemoWxRefundOrderPO;
import com.quaint.demo.wechat.service.DemoPayService;
import com.quaint.demo.wechat.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author quaint
 * @date 2020-01-12 15:57
 */
@Service
@EnableConfigurationProperties(WxProperties.class)
@Slf4j
public class DemoPayServiceImpl implements DemoPayService, InitializingBean {

    @Autowired
    private WxProperties wxProperties;

    private WxPayService wxPayService;

    @Autowired
    private DemoWxPayOrderMapper demoWxPayOrderMapper;

    @Autowired
    private DemoWxLogMapper demoWxLogMapper;

    @Autowired
    private DemoWxRefundOrderMapper demoWxRefundOrderMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,noRollbackFor = Exception.class)
    public Prepay.Response prepay(Prepay.Request request) {

        log.info("获取历史待支付订单开始-->");
        DemoWxPayOrderPO wxPayOrderPo = getHistoryToPayOrder(request);

        if (Objects.nonNull(wxPayOrderPo)){
            log.info("订单[{}]获取到历史支付单，直接返回预支付信息",request.getOrderNo());
            WxPayMpOrderResult wxPayMpOrderResult = JSONObject.parseObject(wxPayOrderPo.getPrepayResult(), WxPayMpOrderResult.class);
            return Prepay.Response.builder()
                    .prepayId(wxPayOrderPo.getPrepayid())
                    .data(wxPayMpOrderResult).build();
        } else{
            log.info("订单[{}]准备创建支付单",request.getOrderNo());
            wxPayOrderPo = new DemoWxPayOrderPO();
            wxPayOrderPo.setOrderCode(request.getOrderNo());
            wxPayOrderPo.setBody(request.getBody());
            wxPayOrderPo.setAmount(request.getAmount());
            // 状态：0.草稿 10.待支付 20.支付成功 30.支付失败
            wxPayOrderPo.setStatus(PayOrderStatusEnum.DRAFT.getValue());
            wxPayOrderPo.setCreateTime(LocalDateTime.now());
            wxPayOrderPo.setUpdateTime(LocalDateTime.now());
            wxPayOrderPo.setValid(true);
            demoWxPayOrderMapper.insert(wxPayOrderPo);
        }

        log.info("准备预支付单请求报文-->");
        // 准备预支付单请求报文
        WxPayUnifiedOrderRequest wxReq = new WxPayUnifiedOrderRequest();
        // 商品简单描述
        wxReq.setBody(wxPayOrderPo.getBody());
        // 商户订单号
        wxReq.setOutTradeNo(wxPayOrderPo.getOrderCode());
        // 金额 分为单位
        wxReq.setTotalFee(wxPayOrderPo.getAmount().multiply(new BigDecimal(100)).intValue());
        // 交易类型
        wxReq.setTradeType("JSAPI");
        // 用户openid
        wxReq.setOpenid(request.getUserId());
        // 回调地址
        wxReq.setNotifyUrl(wxProperties.getPay().getPayNotifyUrl());
        // ip 地址
        wxReq.setSpbillCreateIp(IpUtils.getMachineIP());
        // 设置附件 db , 如订单id
        wxReq.setAttach(String.valueOf(wxPayOrderPo.getId()));

        log.info("订单[{}]创建微信调用日志",request.getOrderNo());
        DemoWxLogPO wxLogPo = new DemoWxLogPO();
        // （1.支付 2. 退款)
        wxLogPo.setSourceType(SourceTypeEnum.PAY.getValue());
        wxLogPo.setSourceId(wxPayOrderPo.getId());
        // (1.主动调用 2.回调通知)
        wxLogPo.setDirection(DirectionEnum.INVOKE.getValue());
        wxLogPo.setRequestBodyLog(JSONObject.toJSONString(wxReq));
        wxLogPo.setCreateTime(LocalDateTime.now());
        wxLogPo.setUpdateTime(LocalDateTime.now());

        try {
            log.info("订单[{}]调用预支付单创建接口",request.getOrderNo());
            WxPayMpOrderResult wxPayMpOrderResult = this.wxPayService.createOrder(wxReq);
            wxLogPo.setResponseBodyLog(JSONObject.toJSONString(wxPayMpOrderResult));

            log.info("订单[{}]生成预支付单成功,保存相关数据",request.getOrderNo());
            // 状态：0.草稿 10.待支付 20.支付成功 30.支付失败
            wxPayOrderPo.setStatus(PayOrderStatusEnum.TO_PAY.getValue());
            wxPayOrderPo.setPrepayid(wxPayMpOrderResult.getPackageValue().split("=")[1]);
            wxPayOrderPo.setPrepayResult(wxLogPo.getResponseBodyLog());
            // 有效期为二小时
            wxPayOrderPo.setExpirationTime(LocalDateTime.now().plusHours(2L));
            demoWxPayOrderMapper.updateById(wxPayOrderPo);
            // 设置结果报文
            return Prepay.Response.builder()
                    .prepayId(wxPayOrderPo.getPrepayid())
                    .data(wxPayMpOrderResult).build();

        }catch (WxPayException e){
            log.error(e.getMessage(),e);
            wxLogPo.setResponseBodyLog(e.getXmlString());
            throw new RuntimeException("微信下预支付单失败");
        }finally {
            // 保存日志
            demoWxLogMapper.insert(wxLogPo);
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,noRollbackFor = Exception.class)
    public Refund.Response refund(Refund.Request request) {

        log.info("根据订单号[{}]查询微信支付单",request.getOrderNo());
        QueryWrapper<DemoWxPayOrderPO> queryOrder = new QueryWrapper<>();
        queryOrder.eq("orderNo",request.getOrderNo());
        DemoWxPayOrderPO wxPayOrderPo = demoWxPayOrderMapper.selectOne(queryOrder);
        if(wxPayOrderPo == null){
            log.error("不存在该支付单[{}]",request.getOrderNo());
            throw new RuntimeException("不存在该支付单");
        }

        log.info("根据退款单[{}]查找微信退款单",request.getRefundOrderNo());
        QueryWrapper<DemoWxRefundOrderPO> queryRefund = new QueryWrapper<>();
        queryOrder.eq("refundOrderNo",request.getRefundOrderNo());
        DemoWxRefundOrderPO wxRefundOrderPO = demoWxRefundOrderMapper.selectOne(queryRefund);
        if(wxRefundOrderPO != null) {
            // 状态：10.退款中 20.退款成功 30.退款失败
            if(!WxRefundOrderStatusEnum.FAILURE.getValue().equals(wxRefundOrderPO.getStatus())){
                log.error("退款单[{}]不能重复申请",wxRefundOrderPO.getRefundOrderCode());
                throw new RuntimeException("不能重复退款");
            }
        }else{
            log.info("退款单[{}]未找到微信退款单,准备创建一个",request.getRefundOrderNo());
            wxRefundOrderPO = new DemoWxRefundOrderPO();
            wxRefundOrderPO.setWxPayOrderId(wxPayOrderPo.getId());
            wxRefundOrderPO.setRefundOrderCode(request.getRefundOrderNo());
            wxRefundOrderPO.setRefundDesc(request.getDesc());
            wxRefundOrderPO.setAmount(request.getAmount());
            // 状态：10.退款中 20.退款成功 30.退款失败
            wxRefundOrderPO.setStatus(WxRefundOrderStatusEnum.REFUNDING.getValue());
            wxRefundOrderPO.setCreateTime(LocalDateTime.now());
            wxRefundOrderPO.setUpdateTime(LocalDateTime.now());
            wxRefundOrderPO.setValid(Boolean.TRUE);
            demoWxRefundOrderMapper.insert(wxRefundOrderPO);
        }
        // 将退款单状态修改为退款中
        wxRefundOrderPO.setStatus(10);

        log.info("退款单[{}]准备请求报文",wxRefundOrderPO.getRefundOrderCode());
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(wxPayOrderPo.getOrderCode());
        wxPayRefundRequest.setOutRefundNo(wxRefundOrderPO.getRefundOrderCode());
        wxPayRefundRequest.setTotalFee(wxPayOrderPo.getAmount().multiply(new BigDecimal(100)).intValue());
        wxPayRefundRequest.setRefundFee(wxRefundOrderPO.getAmount().multiply(new BigDecimal(100)).intValue());
        wxPayRefundRequest.setRefundDesc(wxRefundOrderPO.getRefundDesc());
        wxPayRefundRequest.setNotifyUrl(wxProperties.getPay().getRefundNotifyUrl());

        log.info("退款单[{}]创建微信调用日志",wxRefundOrderPO.getRefundOrderCode());
        DemoWxLogPO wxLogPO = new DemoWxLogPO();
        // 来源类型 （1.支付 2. 退款)
        wxLogPO.setSourceType(SourceTypeEnum.REFUND.getValue());
        // (1.主动调用 2.回调通知)
        wxLogPO.setDirection(DirectionEnum.INVOKE.getValue());
        wxLogPO.setSourceId(wxRefundOrderPO.getId());
        wxLogPO.setRequestBodyLog(JSONObject.toJSONString(wxPayRefundRequest));
        wxLogPO.setCreateTime(LocalDateTime.now());
        wxLogPO.setUpdateTime(LocalDateTime.now());

        try {
            log.info("退款单[{}]调用微信申请退款接口",wxRefundOrderPO.getRefundOrderCode());
            WxPayRefundResult wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);

            log.info("退款单[{}]申请退款成功,保存相关数据",wxRefundOrderPO.getRefundOrderCode());
            wxLogPO.setResponseBodyLog(JSONObject.toJSONString(wxPayRefundResult));
            wxRefundOrderPO.setTransactionId(wxPayRefundRequest.getTransactionId());
            // 设置结果报文
            return Refund.Response.builder()
                    .transationId(wxRefundOrderPO.getTransactionId()).build();
        }catch (WxPayException e){
            log.error(e.getMessage(),e);
            wxLogPO.setResponseBodyLog(e.getXmlString());
            throw new RuntimeException("微信申请退款失败");
        }finally {
            // 保存日志
            demoWxLogMapper.insert(wxLogPO);
            // 更新退款单
            wxRefundOrderPO.setUpdateTime(LocalDateTime.now());
            demoWxRefundOrderMapper.updateById(wxRefundOrderPO);
        }
    }


    /**
     * 支付成功通知
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String payNotify(HttpServletRequest request){

        boolean success = false;
        DemoWxLogPO wxLogPO = new DemoWxLogPO();
        // 异步通知
        wxLogPO.setDirection(DirectionEnum.CALL_BACK.getValue());
        wxLogPO.setSourceType(SourceTypeEnum.PAY.getValue());
        wxLogPO.setUpdateTime(LocalDateTime.now());
        wxLogPO.setCreateTime(LocalDateTime.now());
        String xmlData = null;
        try {
            xmlData = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            log.info("接受到微信请求报文[{}]",xmlData);
            if(StringUtils.isEmpty(xmlData)){
                success = false;
                log.warn("接受到微信请求报文为空[{}]", xmlData);
            }else {
                log.info("准备解析xmlData数据");
                WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(xmlData);

                log.info("根据attach=[{}]查找支付单",wxPayOrderNotifyResult.getAttach());
                Long payOrderId = Long.parseLong(wxPayOrderNotifyResult.getAttach());
                DemoWxPayOrderPO wxPayOrderPO = demoWxPayOrderMapper.selectById(payOrderId);
                if (wxPayOrderPO == null) {
                    throw new RuntimeException("不存在该支付单");
                }
                wxLogPO.setSourceId(payOrderId);

                log.info("支付单[{}]回调,更新相关数据",payOrderId);
                String resultCode = wxPayOrderNotifyResult.getResultCode();
                if(WxPayConstants.ResultCode.SUCCESS.equals(resultCode)){
                    wxPayOrderPO.setStatus(PayOrderStatusEnum.SUCCESS.getValue());
                    wxPayOrderPO.setTransactionId(wxPayOrderNotifyResult.getTransactionId());
                    wxPayOrderPO.setSuccessTime(LocalDateTime.now());
                    wxPayOrderPO.setUpdateTime(LocalDateTime.now());
                    demoWxPayOrderMapper.updateById(wxPayOrderPO);

                    log.info("支付单[{}]发送支付成功事件",payOrderId);
//                    EventPublisher.publish(wxPayOrderPO);
                }else{
                    wxPayOrderPO.setStatus(PayOrderStatusEnum.FAILURE.getValue());
                    wxPayOrderPO.setUpdateTime(LocalDateTime.now());
                    demoWxPayOrderMapper.updateById(wxPayOrderPO);
                }
                success = true;
            }
        }catch (WxPayException ex) {
            log.error("微信支付回调通知处理:" + ex.getCustomErrorMsg(), ex);
        }catch (Exception e) {
            log.error("微信支付回调通知处理:" + e.getMessage(), e);
        }finally {
            if(xmlData != null) {
                wxLogPO.setRequestBodyLog(xmlData);
                wxLogPO.setResponseBodyLog(success
                        ? WxPayNotifyResponse.success("成功")
                        : WxPayNotifyResponse.success("失败"));
                demoWxLogMapper.insert(wxLogPO);
            }
        }
        return wxLogPO.getResponseBodyLog();
    }

    /**
     * 退款成功通知
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String refundNotify(HttpServletRequest request){

        boolean success = false;
        DemoWxLogPO wxLogPO = new DemoWxLogPO();
        wxLogPO.setSourceType(SourceTypeEnum.REFUND.getValue());
        wxLogPO.setDirection(DirectionEnum.CALL_BACK.getValue());
        wxLogPO.setUpdateTime(LocalDateTime.now());
        wxLogPO.setCreateTime(LocalDateTime.now());

        String xmlData = null;
        try {
            xmlData = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            log.info("接受到微信请求报文[{}]",xmlData);
            if(StringUtils.isEmpty(xmlData)) {
                success = false;
                log.warn("接受到微信请求报文为空[{}]", xmlData);
            }else{
                log.info("准备解析xmlData数据");
                WxPayRefundNotifyResult wxPayRefundNotifyResult = wxPayService.parseRefundNotifyResult(xmlData);
                WxPayRefundNotifyResult.ReqInfo reqInfo = wxPayRefundNotifyResult.getReqInfo();
                String transactionId = reqInfo.getTransactionId();

                log.info("根据transactionId=[{}]查找退款单",transactionId);

                DemoWxRefundOrderPO wxRefundOrderPO = demoWxRefundOrderMapper.selectByTransactionId(transactionId);
                if(wxRefundOrderPO == null){
                    throw new RuntimeException("不存在该退款单");
                }
                wxLogPO.setSourceId(wxRefundOrderPO.getId());

                log.info("退款单[{}]回调,更新相关数据",wxRefundOrderPO.getRefundOrderCode());
                if(WxPayConstants.RefundStatus.SUCCESS.equals(reqInfo.getRefundStatus())){
                    wxRefundOrderPO.setStatus(PayOrderStatusEnum.SUCCESS.getValue());
                    wxRefundOrderPO.setSuccessTime(LocalDateTime.now());
                    wxRefundOrderPO.setUpdateTime(LocalDateTime.now());
                    demoWxRefundOrderMapper.updateById(wxRefundOrderPO);

                    log.info("退款单[{}]发送退款成功事件",wxRefundOrderPO.getRefundOrderCode());
//                    EventPublisher.publish(wxRefundOrderPO);
                }else{
                    wxRefundOrderPO.setStatus(PayOrderStatusEnum.FAILURE.getValue());
                    wxRefundOrderPO.setUpdateTime(LocalDateTime.now());
                    demoWxRefundOrderMapper.updateById(wxRefundOrderPO);
                }
                success = true;
            }
        }catch (WxPayException ex) {
            log.error("微信退款回调通知处理:" + ex.getCustomErrorMsg(), ex);
        }catch (Exception e) {
            log.error("微信退款回调通知处理:" + e.getMessage(), e);
        }finally {
            if(xmlData != null) {
                wxLogPO.setRequestBodyLog(xmlData);
                if (success) {
                    wxLogPO.setResponseBodyLog(WxPayNotifyResponse.success("成功"));
                } else {
                    wxLogPO.setResponseBodyLog(WxPayNotifyResponse.success("失败"));
                }
                demoWxLogMapper.insert(wxLogPO);
            }
        }
        return wxLogPO.getResponseBodyLog();
    }

    /**
     * 获取历史待支付订单
     * @param request
     * @return
     */
    private DemoWxPayOrderPO getHistoryToPayOrder(Prepay.Request request){

        DemoWxPayOrderPO historyToPayOrder = null;
        // 删除过期支付单
        demoWxPayOrderMapper.deleteExpireToPayOrder(request.getOrderNo());
        // 查询历史有效订单
        List<DemoWxPayOrderPO> historyWxPayOrderList = demoWxPayOrderMapper.selectPayOrderList(request.getOrderNo());
        if(!CollectionUtils.isEmpty(historyWxPayOrderList)){
            // 是否有支付成功的历史订单
            boolean hasSuccessPayOrder = historyWxPayOrderList.stream().filter(historyWxPayOrder -> PayOrderStatusEnum.SUCCESS.equals(historyWxPayOrder.getStatus())).findFirst().isPresent();
            if(hasSuccessPayOrder){
                throw new RuntimeException("已经支付成功");
            }
            // 历史待支付订单列表
            List<DemoWxPayOrderPO> historyToPayOrderList = historyWxPayOrderList.stream().filter(historyWxPayOrder -> PayOrderStatusEnum.TO_PAY.equals(historyWxPayOrder)).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(historyToPayOrderList)){
                if(historyToPayOrderList.size()>1){
                    throw new RuntimeException("有多个待支付订单");
                }
                historyToPayOrder = historyToPayOrderList.get(0);
            }
        }
        return historyToPayOrder;
    }


    @Override
    public void afterPropertiesSet() {

        WxPayConfig payConfig = new WxPayConfig();
        WxProperties.PayProperties payProperties = this.wxProperties.getPay();
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
