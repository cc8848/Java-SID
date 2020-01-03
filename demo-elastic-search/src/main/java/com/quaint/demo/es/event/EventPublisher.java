package com.quaint.demo.es.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * @author quaint
 * @date 2020-01-03 22:36
 */
@Service
public class EventPublisher implements ApplicationEventPublisherAware {

    private static ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

    /**
     * 发布事件
     * @param event e
     */
    public static void publish(ApplicationEvent event){
        if (publisher != null){
            publisher.publishEvent(event);
        }
    }

    /**
     * 发布事件
     * @param event e
     */
    public static void publish(Object event){
        if (publisher != null){
            publisher.publishEvent(event);
        }
    }

}
