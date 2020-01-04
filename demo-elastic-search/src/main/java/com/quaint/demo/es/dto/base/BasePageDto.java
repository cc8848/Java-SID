package com.quaint.demo.es.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数封装，用于统一使用分页属性
 * @author quaint
 * @date 2019-12-27 11:23
 */
@Data
@ApiModel(value = "分页基础dto", description = "用来传递处理参数,返回结果集对象的基础类")
public class BasePageDto implements Serializable {

    @ApiModelProperty("开始页数")
    private Integer pageNum;

    @ApiModelProperty("页数大小")
    private Integer pageSize;

    /**
     * 检查是否非空
     * @return bool
     */
    public boolean check() {
        if (pageNum != null && pageSize != null){
            return true;
        }
        // 若检查不通过, 全部视为null
        pageNum = null;
        pageSize = null;
        return false;
    }

    /**
     * 标准化page入参
     * @return bool
     */
    public boolean standardization() {
        if (check()) {
            pageNum = pageNum > 0 ? pageNum : 1;
            pageSize = pageSize > 0 ? pageSize : 20;
            return true;
        }
        return false;
    }

    /**
     * 标准化page入参(defaultPageSize)
     * @param defaultPageSize 设置如果pageSize 如果<=0 则取该值
     * @return bool
     */
    public boolean standardization(int defaultPageSize) {
        if (check()) {
            pageNum = pageNum > 0 ? pageNum : 1;
            pageSize = pageSize > 0 ? pageSize : defaultPageSize;
            return true;
        }
        return false;
    }

    /**
     * 计算limit 分页 startNum
     * @return
     */
    public Integer calculationStartNum(){
        if (standardization()){
            return (pageNum-1)*pageSize;
        }
        return null;
    }

    /**
     * 计算limit 分页 startNum (defaultPageSize)
     * @param defaultPageSize 设置如果pageSize 如果<=0 则取该值
     * @return
     */
    public Integer calculationStartNum(int defaultPageSize){
        if (standardization(defaultPageSize)){
            return (pageNum-1)*pageSize;
        }
        return null;
    }

    /**
     * es标准化page入参,只初始化一次就好,多次会有多次减1的问题
     * @return bool
     */
    public boolean esStandardization() {
        if (check()) {
            // es 分页从 第0页开始,pageNum需要减1, 默认展示10条
            pageNum = pageNum > 0 ? pageNum-1 : 0;
            pageSize = pageSize > 0 ? pageSize : 10;
            return true;
        }
        return false;
    }

    /**
     * es标准化page入参,只初始化一次就好,多次会有多次减1的问题(defaultPageSize)
     * @param defaultPageSize 设置如果pageSize 如果<=0 则取该值
     * @return bool
     */
    public boolean esStandardization(int defaultPageSize) {
        if (check()) {
            // es 分页从 第0页开始,pageNum需要减1, 默认展示10条
            pageNum = pageNum > 0 ? pageNum-1 : 0;
            pageSize = pageSize > 0 ? pageSize : defaultPageSize;
            return true;
        }
        return false;
    }

    public BasePageDto(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public BasePageDto() {
    }
}
