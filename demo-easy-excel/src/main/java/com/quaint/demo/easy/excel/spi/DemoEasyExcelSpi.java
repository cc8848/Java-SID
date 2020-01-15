package com.quaint.demo.easy.excel.spi;

import com.alibaba.excel.annotation.ExcelProperty;
import com.quaint.demo.easy.excel.dto.DemoUserDto;
import com.quaint.demo.easy.excel.utils.EasyExcelUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author quaint
 * @date 2020-01-14 11:13
 */
@Controller
public class DemoEasyExcelSpi {

    @PostMapping("/in/excel")
    public String inExcel(@RequestParam("inExcel") MultipartFile inExcel, Model model){

        if (inExcel.isEmpty()){
            // 读取 local 指定文件
            List<DemoUserDto> demoUserList = EasyExcelUtils.readLocalExcel("ExcelTest", DemoUserDto.class);
            model.addAttribute("users", demoUserList);
        } else {
            // 读取 web 上传的文件
            List<DemoUserDto> demoUserList = EasyExcelUtils.readWebExcel(inExcel, DemoUserDto.class);
            model.addAttribute("users", demoUserList);
        }
        return "index";
    }

    @PostMapping("/out/excel")
    public void export(HttpServletResponse response){

        String search = "@RequestBody Object search";
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
                    // 忽略 该字段
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
        EasyExcelUtils.exportWebExcel(response,userDto,DemoUserDto.class,"ExcelTest",null);

    }
}
