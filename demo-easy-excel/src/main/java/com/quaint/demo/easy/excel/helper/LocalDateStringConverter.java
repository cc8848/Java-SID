package com.quaint.demo.easy.excel.helper;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate and string converter
 *
 * @author quait
 */
public class LocalDateStringConverter implements Converter<LocalDate> {

    @Override
    public Class supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration){
        // 将excel 中的 数据 转换为 LocalDate
        return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public CellData convertToExcelData(LocalDate value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        // 将 LocalDate 转换为 String
        return new CellData(value.toString());
    }
}