package com.quaint.demo.easy.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author quaint
 * @date 2020-01-14 11:23
 */
@Data
public class DemoParentDto {

    @ExcelProperty(index = 0,value = {"序号"})
    private Integer num;

}
