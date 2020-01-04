package com.quaint.demo.es.enums;

/**
 * @author quaint
 * @date 2020-01-04 16:30
 */
public enum DataGetMode {

    /**
     * 引用
     */
    REFERENCE(1),
    /**
     * 实体中的id
     */
    ID_OF_ENTITY(2);

    private int val;

    DataGetMode(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
