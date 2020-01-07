package com.quaint.demo.wechat.enums;

/**
 * @author quaint
 * @date 2020-01-07 14:04
 */
public enum WxTypeEnum {

    /**
     * 公众号
     */
    WX_TYPE_MP(1,"公众号"),
    /**
     * 小程序
     */
    WX_TYPE_MA(2,"小程序");

    private Integer type;
    private String desc;

    WxTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static WxTypeEnum getEnumByType(Integer type){
        if (type == null){
            return null;
        }
        for (WxTypeEnum w : WxTypeEnum.values()){
            if (w.getType().equals(type)){
                return w;
            }
        }
        return null;
    }

}
