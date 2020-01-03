package com.quaint.demo.es.handler;

import com.quaint.demo.es.enums.DataType;

/**
 * 数据变化处理器
 * @author quaint
 * @date 2020-01-03 15:02
 */
public interface IndexDataHandler<T> {

    /**
     * 刷新数据
     */
    void refresh();

    /**
     * 处理数据变化时间
     * @param pkId 数据id
     */
    void handleChange(T pkId);


    /**
     * 支持的类型
     * @param dataType 类型
     * @return bool
     */
    boolean support(DataType dataType);

}
