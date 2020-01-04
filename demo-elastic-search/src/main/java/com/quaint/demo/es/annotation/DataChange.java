package com.quaint.demo.es.annotation;

import com.quaint.demo.es.enums.DataGetMode;

import java.lang.annotation.*;

/**
 * 标明该方法需要触发dataChange事件
 * @author quaint
 * @date 2020-01-04 16:37
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataChange {

    /**
     * 数据获取类型
     * @return
     */
    DataGetMode value() default DataGetMode.REFERENCE;

}
