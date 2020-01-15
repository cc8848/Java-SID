package com.quaint.demo.easy.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.quaint.demo.easy.excel.handler.CustomCellWriteHandler;
import com.quaint.demo.easy.excel.listener.DemoUserListener;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * EasyExcelUtils
 * @author quaint
 * @date 2020-01-14 14:26
 */
public abstract class EasyExcelUtils {


    /**
     * 读取 local excel demo
     * @param fileName 本地文件名称
     * @param clazz 指定转化类
     * @param <T> 转化类泛型
     * @return 本地导入的数据
     */
    public static <T> List<T> readLocalExcel(String fileName, Class<T> clazz){

        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        String filePath = System.getProperty("user.dir")+"/demo-easy-excel/src/main/resources/"+fileName+".xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        DemoUserListener demoUserListener = new DemoUserListener();
        try {
            EasyExcel.read(filePath, clazz,demoUserListener).sheet().doRead();
            return (List<T>) demoUserListener.getVirtualDataBase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取web excel demo
     * @param file web 文件
     * @param clazz 指定转化类
     * @param <T> 类的泛型
     * @return web导入的数据
     */
    public static <T> List<T> readWebExcel(MultipartFile file, Class<T> clazz){

        DemoUserListener demoUserListener = new DemoUserListener();
        try {
            EasyExcel.read(file.getInputStream(), clazz, demoUserListener).sheet().doRead();
            return (List<T>) demoUserListener.getVirtualDataBase();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 导出excel
     * @param response http下载
     * @param dataList 导出的数据
     * @param clazz 导出的模板类
     * @param fileName 导出的文件名
     * @param excludeFiledNames 要排除的filed
     * @param <T> 模板
     */
    public static <T> void exportWebExcel(HttpServletResponse response, List<T> dataList, Class<T> clazz,
                                 String fileName, Set<String> excludeFiledNames) {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        try {
            // 导出excel
            EasyExcel.write(response.getOutputStream(), clazz)
                    .excludeColumnFiledNames(excludeFiledNames)
                    .sheet("fileName")
                    .doWrite(dataList);
        } catch (IOException e) {
            System.err.println("创建文件异常!");
        }

    }

    /**
     * 导出excel
     * @param dataList 导出的数据
     * @param clazz 导出的模板类
     * @param fileName 导出的文件名
     * @param excludeFiledNames 要排除的filed
     * @param <T> 模板
     */
    public static <T> void exportLocalExcel(List<T> dataList, Class<T> clazz, String fileName,
                                            Set<String> excludeFiledNames){
        //创建本地文件 test 使用
        String filePath = System.getProperty("user.dir")+"/demo-easy-excel/src/main/resources/"+fileName+".xlsx";

        File dbfFile = new File(filePath);
        if (!dbfFile.exists() || dbfFile.isDirectory()) {
            try {
                dbfFile.createNewFile();
            } catch (IOException e) {
                System.err.println("创建文件异常!");
                return;
            }
        }

        // 导出excel
        EasyExcel.write(filePath, clazz)
                .registerWriteHandler(new CustomCellWriteHandler())
                .excludeColumnFiledNames(excludeFiledNames)
                .sheet("SheetName").doWrite(dataList);


    }

}
