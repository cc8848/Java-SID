package com.quaint.demo.wechat.service;

import com.quaint.demo.wechat.dto.Prepay;
import com.quaint.demo.wechat.dto.Refund;

/**
 * @author quaint
 * @date 2020-01-12 15:57
 */
public interface DemoPayService {

    /**
     * 生成预支付单
     * @param request req
     * @return return
     */
    Prepay.Response prepay(Prepay.Request request);

    /**
     * 执行退款
     * @param request req
     * @return return
     */
    Refund.Response refund(Refund.Request request);

}
