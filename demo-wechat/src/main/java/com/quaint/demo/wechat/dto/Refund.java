package com.quaint.demo.wechat.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;

/**
 * 退款
 * @author quaint
 * @date 2020-01-12 16:02:01
 */
public interface Refund {

    @Data
    class Request{
        // 订单编号
        private String orderNo;
        // 退款单编号
        private String refundOrderNo;
        // 退款金额
        private BigDecimal amount;
        // 退款原因
        private String desc;
    }

    @Data
    @Builder
    class Response{
        // 预支付id
        private String transationId;
        // 原始报文
        private Object data;

        @Tolerate
        public Response(){}
    }
}
