package com.quaint.demo.wechat.enums.pay;

/**
 * 微信退款单状态
 *
 * @author quaint
 */
public enum WxRefundOrderStatusEnum {

    /**
     * 微信退款单状态
     */
    REFUNDING(10,"退款中"),
    SUCCESS(20,"退款成功"),
    FAILURE(30,"退款失败"),
    ;

    private Integer value;
    private String text;

    WxRefundOrderStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
