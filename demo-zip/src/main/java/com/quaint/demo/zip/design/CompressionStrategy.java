package com.quaint.demo.zip.design;

import java.io.InputStream;
import java.util.List;

/**
 * @author quaint
 * @date 15 February 2020
 * @since 1.30
 */
public interface CompressionStrategy <T> {

    /**
     * 是否支持
     * @param fileName 文件名称
     * @return true
     */
    boolean support(String fileName);


    /**
     * 提取策略
     * @param inputStream 文件
     * @return 数据
     */
    List<T> extract(InputStream inputStream);

    /**
     * 压缩策略
     * @param dataList 数据
     * @return 文件
     */
    InputStream compression(List<T> dataList);

}
