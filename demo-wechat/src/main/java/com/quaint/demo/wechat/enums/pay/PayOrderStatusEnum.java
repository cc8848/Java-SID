package com.quaint.demo.wechat.enums.pay;

/**
 * 支付单状态枚举
 *
 * @author quaint
 */
public enum PayOrderStatusEnum {

    /**
     * 支付单状态枚举
     */
    DRAFT(0,"草稿"),
    TO_PAY(10,"待支付"),
    SUCCESS(20,"支付成功"),
    FAILURE(30,"支付失败"),
    INVALID(-10,"已失效")
    ;

    private Integer value;
    private String text;

    PayOrderStatusEnum(Integer value, String text) {
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
