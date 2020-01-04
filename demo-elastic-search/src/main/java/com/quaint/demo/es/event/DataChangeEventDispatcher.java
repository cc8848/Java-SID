package com.quaint.demo.es.event;

import com.quaint.demo.es.handler.IndexDataHandler;
import com.quaint.demo.es.utils.ThreadPoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

/**
 * 事件分发器
 * @author quaint
 * @date 2020-01-03 22:57
 */
@Component
public class DataChangeEventDispatcher implements EventCommitAfterListener<DataChangeEvent> {

    /**
     * 注入所有实现 IndexDataHandler 接口的类
     */
    @Autowired
    List<IndexDataHandler> indexDataHandlerList;

    @Override
    public void eventHandling(DataChangeEvent event) {
        if(!CollectionUtils.isEmpty(indexDataHandlerList)){
            Flux.fromIterable(indexDataHandlerList)
                    // 找出支持该dataType的handler
                    .filter(dataChangeHandler -> dataChangeHandler.support(event.getDataType()))
                    // 指定线程池
                    .publishOn(Schedulers.fromExecutorService(ThreadPoolUtils.getExecutorService()))
                    // 对符合条件的indexDataHandler迭代处理变化
                    .subscribe(dataChangeHandler -> dataChangeHandler.handleChange(event.getData()));

        }
    }



}
