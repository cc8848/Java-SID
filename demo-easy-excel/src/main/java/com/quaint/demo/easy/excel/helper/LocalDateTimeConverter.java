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
        // 将excel 中的 数据 转换为 LocalDateTime
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            // 获取注解的 format  注意,注解需要导入这个 excel.annotation.format.DateTimeFormat;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
            return LocalDateTime.parse(cellData.getStringValue(), formatter);
        }
    }

    @Override
    public CellData convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        // 将 LocalDateTime 转换为 String
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new CellData(value.toString());
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
            return new CellData(value.format(formatter));
        }
    }
}