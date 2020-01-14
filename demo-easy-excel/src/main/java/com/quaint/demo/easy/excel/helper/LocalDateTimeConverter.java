package com.quaint.demo.easy.excel.helper;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime and string converter
 *
 * @author quait
 */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    @Override
    public Class supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration){
        // 将excel 中的 数据 转换为 LocalDate
        return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public CellData convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        // 将 LocalDate 转换为 String
        return new CellData(value.toString());
    }
}