package com.quaint.demo.zip.design;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     * @throws IOException io
     */
    List<T> extract(InputStream inputStream) throws IOException;

    /**
     * 压缩策略
     * @param dataList 数据
     * @param os 输出流
     * @throws IOException io
     */
    void compression(List<T> dataList, OutputStream os) throws IOException;

}
