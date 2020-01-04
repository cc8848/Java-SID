package com.quaint.demo.es.enums;

/**
 * @author quaint
 * @date 2020-01-03 15:04
 */
public enum DataType {

    /**
     * item 类型
     */
    ITEM_TYPE(1,"测试Item类型"),
    /**
     * demo article type
     */
    DEMO_ARTICLE_TYPE(2,"demo article type")
    ;

    private Integer val;
    private String text;

    public Integer getVal() {
        return val;
    }

    public String getText() {
        return text;
    }

    DataType(Integer val, String text) {
        this.val = val;
        this.text = text;
    }

    /**
     * 通过 val 获取 text
     * @param val
     * @return
     */
    public String getTextByVal(Integer val){

        if (val == null){
            return null;
        }
        for (DataType dt : values()){
            if (dt.getVal().equals(val)){
                return dt.getText();
            }
        }
        return null;
    }

}
