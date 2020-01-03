package com.quaint.demo.es.event;

import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author quaint
 * @date 2020-01-03 22:32
 */
public interface EventCommitedListener<T> {

    /**
     * 处理事件
     * @param event e
     */
    @TransactionalEventListener
    void eventHandling(T event);

}
