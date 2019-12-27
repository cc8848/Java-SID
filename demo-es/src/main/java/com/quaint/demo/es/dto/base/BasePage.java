package com.quaint.demo.es.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author quaint
 * @date 2019-12-27 11:23
 */
@Data
@ApiModel("分页基础dto")
public class BasePage {

    @ApiModelProperty("开始页数")
    private Integer pageNum;

    @ApiModelProperty("页数大小")
    private Integer pageSize;

    /**
     * 检查是否非空
     * @return bool
     */
    public boolean check() {
        return pageNum != null && pageSize != null;
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

}
