package com.quaint.demo.easy.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.quaint.demo.easy.excel.helper.LocalDateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 虽然 OrderMap 可以完全复用 ReviewMap 的字段,
 * 但是 继承后 导出excel 顺序会有先子类,后父类问题,
 * 而且反射还需要读取父类忽略的属性,这里做了尝试样例
 *
 * @author quaint
 * @date 2020-01-14 11:20
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemoUserDto extends DemoParentDto{

    @ExcelProperty(value = {"姓名"})
    private String name;

    @ExcelProperty(value = {"性别"})
    private String sex;

    /**
     * @see LocalDateConverter
     */
    @ExcelProperty(value = "生日",converter = LocalDateConverter.class)
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate birthday;

    @ExcelProperty(value = {"存款"})
    private BigDecimal money;


    /**
     * 获取6个测试数据
     * @return 6个
     */
    public static List<DemoUserDto> getUserDtoTest6(String search){
        List<DemoUserDto> list = new ArrayList<>();
        list.add(new DemoUserDto("quaint","男",LocalDate.of(2011,11,11),BigDecimal.ONE));
        list.add(new DemoUserDto("quaint2","女",LocalDate.of(2001,11,1),BigDecimal.TEN));
        list.add(new DemoUserDto("quaint3","男",LocalDate.of(2010,2,7),new BigDecimal(11.11)));
        list.add(new DemoUserDto("quaint4","男",LocalDate.of(2011,1,11),new BigDecimal(10.24)));
        list.add(new DemoUserDto("quaint5","女",LocalDate.of(2021,5,12),BigDecimal.ZERO));
        list.add(new DemoUserDto(search,"男",LocalDate.of(2010,7,11),BigDecimal.TEN));
        return list;
    }

}
