package com.quaint.demo.es.search.handler.abst;

import com.quaint.demo.es.utils.ThreadPoolUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author quaint
 * @date 2020-01-03 15:16
 */
public abstract class AbstractDataChangeHandler<T> implements IndexDataHandler<T> {

    /**
     * 分批处理
     * @param dataList 数据list
     * @param onNext 处理流程
     * @param <D> 类型
     */
    protected final <D> void batchUpdate(List<D> dataList, Consumer<D> onNext){

        // 迭代处理数据
//        dataList.forEach(onNext);

        // 迭代进行数据处理
        Flux.fromIterable(dataList)
                .publishOn(Schedulers.fromExecutorService(ThreadPoolUtils.getExecutorService()))
                .doOnNext(onNext)
                .blockLast();

    }

}
