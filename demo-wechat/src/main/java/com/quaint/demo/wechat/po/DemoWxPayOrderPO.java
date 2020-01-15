package com.quaint.demo.wechat.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author quaint
 * @date 2020-01-15 13:45
 */
@TableName("demo_wx_pay_order")
@Data
public class DemoWxPayOrderPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_code")
    private String orderCode;

    /**
     * 商品简单描述
     */
    private String body;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 状态：0.草稿 10.待支付 20.支付成功 30.支付失败
     */
    private Integer status;

    /**
     * 预支付id
     */
    private String prepayid;

    /**
     * 预支付响应报文
     */
    @TableField("prepay_result")
    private String prepayResult;

    /**
     * 微信订单号
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 成功时间
     */
    @TableField("success_time")
    private LocalDateTime successTime;

    /**
     * 失效时间
     */
    @TableField("expiration_time")
    private LocalDateTime expirationTime;

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
