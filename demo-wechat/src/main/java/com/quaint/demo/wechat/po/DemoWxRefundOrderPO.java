package com.quaint.demo.wechat.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author quaint
 * @date 2020-01-15 13:42
 */
@TableName("demo_wx_refund_order")
@Data
public class DemoWxRefundOrderPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 微信支付单id
     */
    @TableField("wx_pay_order_id")
    private Long wxPayOrderId;

    /**
     * 退款单号
     */
    @TableField("refund_order_code")
    private String refundOrderCode;

    /**
     * 退款描述
     */
    @TableField("refund_desc")
    private String refundDesc;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 状态：10.退款中 20.退款成功 30.退款失败
     */
    private Integer status;

    /**
     * 微信退款单号
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 成功时间
     */
    @TableField("success_time")
    private LocalDateTime successTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 是否有效：1.是 0.否
     */
    @TableLogic
    private Boolean valid;

}
