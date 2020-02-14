package com.quaint.demo.easy.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 自定义写Excel handler 实现style 策略。
 * @author quaint
 * @date 14 February 2020
 * @since 1.30
 */
@Slf4j
@Component
@Scope(SCOPE_PROTOTYPE)
public class ProductWriteErrHandler extends AbstractCellStyleStrategy {


    /**
     * 存储解析失败的行号和列号
     */
    private Map<Integer, Integer> failureRowCol;
    
    /**
     * 可以这么理解: 外部定义样式
     */
    private WriteCellStyle writeErrCellStyle;

    /**
     * 单元格样式
     */
    private CellStyle errCellStyle;

    /**
     * 在这里自定义样式, 或者在外面定义样式
     */
    public ProductWriteErrHandler(WriteCellStyle writeCellStyle,Map<Integer, Integer> failureRowCol) {
        this.writeErrCellStyle = writeCellStyle;
        this.failureRowCol = failureRowCol;
    }

    /**
     * 单元格样式初始化方法
     * @param workbook
     */
    @Override
    protected void initCellStyle(Workbook workbook) {
        // 初始化
        if (writeErrCellStyle!=null){
            errCellStyle = StyleUtil.buildContentCellStyle(workbook, writeErrCellStyle);
        }
    }

    /**
     * 写头部样式
     * @param cell
     * @param head
     * @param relativeRowIndex
     */
    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }

    /**
     * 写内容样式
     * @param cell
     * @param head
     * @param relativeRowIndex
     */
    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        // 判断 是否传入 错误的 map
        if (!CollectionUtils.isEmpty(failureRowCol)){
            // 如果错误 的行 和列 对应成功 --> 染色
            if (failureRowCol.containsKey(cell.getRowIndex()) 
                    && failureRowCol.get(cell.getRowIndex()).equals(cell.getColumnIndex())){
                cell.setCellStyle(errCellStyle);
            }
        }
    }

}