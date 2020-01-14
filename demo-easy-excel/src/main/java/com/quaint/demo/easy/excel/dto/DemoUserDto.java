package com.quaint.demo.easy.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.quaint.demo.easy.excel.helper.LocalDateStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author quaint
 * @date 2020-01-14 11:20
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class DemoUserDto extends DemoParentDto{

    @ExcelProperty(value = {"姓名"})
    private String name;

    @ExcelProperty(value = {"性别"})
    private String sex;

    /**
     * @see LocalDateStringConverter
     */
    @ExcelProperty(value = "生日",converter = LocalDateStringConverter.class)
    private LocalDate birthday;

    @ExcelProperty(value = {"年龄"})
    private Integer age;


    /**
     * 获取6个测试数据
     * @return 6个
     */
    public static List<DemoUserDto> getUserDtoTest6(Object search){
        List<DemoUserDto> list = new ArrayList<>();
        list.add(new DemoUserDto("quaint","男",LocalDate.of(2011,11,11),9));
        list.add(new DemoUserDto("quaint2","女",LocalDate.of(2001,11,1),19));
        list.add(new DemoUserDto("quaint3","男",LocalDate.of(2010,2,7),10));
        list.add(new DemoUserDto("quaint4","男",LocalDate.of(2011,1,11),9));
        list.add(new DemoUserDto("quaint5","女",LocalDate.of(2021,5,12),-1));
        list.add(new DemoUserDto("quaint6","男",LocalDate.of(2010,7,11),10));
        return list;
    }

}
