package com.quaint.demo.es.utils;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author quaint
 * @date 2020-01-03 23:19
 */
public abstract class ThreadPoolUtils {

    private static volatile ExecutorService executorService;

    /**
     * 获取线程池
     * @return executorService
     */
    public static ExecutorService getExecutorService() {
        if (executorService == null){
            synchronized (ThreadPoolUtils.class){
                if (executorService == null){
                    executorService = new ThreadPoolExecutor(5,
                            10,
                            0,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(1024),
                            new CustomizableThreadFactory("es-thread-pool"),
                            new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
        return executorService;
    }

    /**
     * 提交一个任务 给线程池处理
     * @param task
     */
    public static void submit(Runnable task){
        ExecutorService executorService = getExecutorService();
        executorService.submit(task);
    }

}
