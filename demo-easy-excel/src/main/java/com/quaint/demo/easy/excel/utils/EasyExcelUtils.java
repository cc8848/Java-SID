package com.quaint.demo.easy.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.quaint.demo.easy.excel.handler.CustomCellWriteHandler;
import com.quaint.demo.easy.excel.handler.ProductWriteErrHandler;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EasyExcelUtils  工具类是静态的, 就不以getBean 形式展示了...
 * @author quaint
 * @date 2020-01-14 14:26
 */
public abstract class EasyExcelUtils {


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

        // 单元格样式策略 定义
        WriteCellStyle style = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        style.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.RED.getIndex());

        Map<Integer,Integer> errRecord = new HashMap<>();
        errRecord.put(1,1);
        errRecord.put(2,2);
        ProductWriteErrHandler handler = new ProductWriteErrHandler(style,errRecord);


        try {
            // 导出excel
            EasyExcel.write(response.getOutputStream(), clazz)
                    // 设置过滤字段策略
                    .excludeColumnFiledNames(excludeFiledNames)
                    // 选择导入时的 handler
                    .registerWriteHandler(handler)
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
