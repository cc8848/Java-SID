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
public class LocalDateConverter implements Converter<LocalDate> {

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
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            // 获取注解的 format  注意,注解需要导入这个 excel.annotation.format.DateTimeFormat;
            return LocalDate.parse(cellData.getStringValue(),
                    DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat()));
        }
    }

    @Override
    public CellData convertToExcelData(LocalDate value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        // 将 LocalDateTime 转换为 String
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new CellData(value.toString());
        } else {
            return new CellData(value.format(DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat())));
        }
    }
}