package com.quaint.demo.es.handler;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author quaint
 * @date 2020-01-03 15:16
 */
public abstract class AbstractDataChangeHandler<T> implements IndexDataHandler<T> {

    /**
     * 分批处理 暂未实现
     * @param dataList
     * @param onNext
     * @param <D>
     */
    protected final <D> void batchUpdate(List<D> dataList, Consumer<D> onNext){


    }

}
