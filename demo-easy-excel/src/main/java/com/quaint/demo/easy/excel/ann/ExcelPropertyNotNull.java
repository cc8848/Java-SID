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
     * @return 开启校验
     */
    boolean open() default true;

    /**
     * @return 提示消息
     */
    String message() default "";

    /**
     * @return 列号
     */
    int col() default -1;

}
