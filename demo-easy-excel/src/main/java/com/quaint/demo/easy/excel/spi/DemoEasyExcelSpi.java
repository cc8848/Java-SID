package com.quaint.demo.easy.excel.spi;

import com.alibaba.excel.annotation.ExcelProperty;
import com.quaint.demo.easy.excel.dto.DemoUserDto;
import com.quaint.demo.easy.excel.utils.EasyExcelUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author quaint
 * @date 2020-01-14 11:13
 */
@RestController
public class DemoEasyExcelSpi {

    @PostMapping("/out/excel")
    public void export(HttpServletResponse response){

        Object search = "@RequestBody Object search";
        // 根据前端传入的查询条件 去库里查到要导出的dto
        List<DemoUserDto> userDto = DemoUserDto.getUserDtoTest6(search);
        // 要忽略的 字段
        List<String> ignoreIndices = Collections.singletonList("性别");

        // 根据类型获取要反射的对象
        Class clazz = DemoUserDto.class;

        // 遍历所有字段, 找到忽略的字段
        Set<String> excludeFiledNames = new HashSet<>();
        while (clazz != Object.class){
            Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
                ExcelProperty ann = field.getAnnotation(ExcelProperty.class);
                if (ann!=null && ignoreIndices.contains(ann.value()[0])){
                    // json 忽略 该字段
                    excludeFiledNames.add(field.getName());
                }
            });
            clazz = clazz.getSuperclass();
        }

        // 设置序号
        AtomicInteger i = new AtomicInteger(1);
        userDto.forEach(u-> u.setNum(i.getAndIncrement()));

        // 创建本地文件
        EasyExcelUtils.exportLocalExcel(userDto,DemoUserDto.class,"ExcelTest",excludeFiledNames);
        // 创建web文件
//        EasyExcelUtils.exportWebExcel(response,userDto,DemoUserDto.class,"ExcelTest",null);

    }
}
