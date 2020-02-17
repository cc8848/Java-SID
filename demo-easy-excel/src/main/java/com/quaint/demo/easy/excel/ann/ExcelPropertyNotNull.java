package com.quaint.demo.easy.excel.ann;

import java.lang.annotation.*;

/**
 * @author quaint
 * @since 17 February 2020
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelPropertyNotNull {

    /**
     * 开启校验
     * @return
     */
    boolean open() default true;

    /**
     * 提示消息
     * @return
     */
    String message() default "";

}
