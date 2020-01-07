package com.quaint.demo.wechat.config.helper;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.List;
import java.util.Objects;

/**
 * json 排除策略
 * @author quaint
 * @date 2020-01-07 15:50
 */
public class JsonExclusionStrategy implements ExclusionStrategy {

    private List<String> skipFields;

    private Class<?> aClass;

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        if (Objects.isNull(skipFields)){
            return false;
        }
        return skipFields.contains(fieldAttributes.getName());
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        if (Objects.isNull(this.aClass)){
            return false;
        }
        return this.aClass.equals(aClass);
    }

    public JsonExclusionStrategy(List<String> skipFields) {
        this.skipFields = skipFields;
    }

    public JsonExclusionStrategy(Class<?> aClass) {
        this.aClass = aClass;
    }

    public JsonExclusionStrategy(List<String> skipFields, Class<?> aClass) {
        this.skipFields = skipFields;
        this.aClass = aClass;
    }
}
