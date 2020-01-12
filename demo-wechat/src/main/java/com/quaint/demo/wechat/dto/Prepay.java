package com.quaint.demo.wechat.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;

/**
 * 预支付
 * @author quaint
 * @date 2020-01-12 16:02:01
 */
public interface Prepay {

    @ApiModel("预支付请求")
    @Data
    class Request{

        // 订单编号
        private String orderNo;
        // 支付金额
        private BigDecimal amount;
        // 用户标识(微信对应openId)
        private String userId;
    }

    @ApiModel("预支付响应")
    @Data
    @Builder
    class Response{
        // 预支付id
        private String prepayId;
        // 原始报文
        private Object data;

        @Tolerate
        public Response(){}
    }
}
