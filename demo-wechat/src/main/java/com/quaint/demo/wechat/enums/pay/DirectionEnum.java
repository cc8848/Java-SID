package com.quaint.demo.wechat.enums.pay;

/**
 * 日志方向
 *
 * @author quaint
 */
public enum DirectionEnum {

    /**
     * 日志方向
     */
    INVOKE(1,"主动调用"),
    CALL_BACK(2,"异步通知"),
    ;

    private Integer value;
    private String text;

    DirectionEnum(Integer value, String text) {
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
