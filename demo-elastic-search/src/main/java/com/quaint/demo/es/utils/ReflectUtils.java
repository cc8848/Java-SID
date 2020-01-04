package com.quaint.demo.es.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author quaint
 * @date 2020-01-04 16:40
 */
public abstract class ReflectUtils {

    /**
     * 获取属性值
     * @param propertyName
     * @param obj
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object getPropertyValue(String propertyName, Object obj) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        return BeanUtils.getPropertyDescriptor(obj.getClass(),propertyName).getReadMethod().invoke(obj);
    }

}
