package com.quaint.demo.wechat.enums.pay;

/**
 * 日志来源类型
 *
 * @author quaint
 */
public enum SourceTypeEnum {

    /**
     * 日志来源类型
     */
    PAY(1,"支付"),
    REFUND(2,"退款");

    private Integer value;
    private String text;

    SourceTypeEnum(Integer value, String text) {
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
