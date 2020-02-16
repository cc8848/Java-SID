package com.quaint.demo.zip.design;

import com.quaint.demo.zip.dto.ImageDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author quaint
 * @date 15 February 2020
 * @since 1.30
 */
@Component
public class ZipImageStrategy implements CompressionStrategy<ImageDto> {

    /**
     * 传入文件类型
     */
    private static final String ZIP_FORMAT = ".zip";

    /**
     * 目标类型
     */
    private static final List<String> TARGET_TYPE = Arrays.asList(".png", ".jpeg", ".jpg", ".gif");

    @Override
    public boolean support(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return false;
        }
        return fileName.endsWith(ZIP_FORMAT);
    }

    @Override
    public List<ImageDto> extract(InputStream inputStream) throws IOException {

        if (inputStream == null){
            return null;
        }

        // 定义储存数据的list
        List<ImageDto> dataList = new ArrayList<>();

        // 把输入流 包装为 压缩流
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {

            String name = ze.getName();
            // 过滤掉 多余的文件/不是图片的文件
            if (ze.isDirectory() || name == null || name.contains("__MACOSX") || name.contains(".DS_Store")
                    || !TARGET_TYPE.contains(name.substring(name.lastIndexOf('.')))) {
                continue;
            }

            // 添加图片到集合
            ImageDto imageDto = new ImageDto();
            imageDto.setFileName(name.substring(name.lastIndexOf(File.separator) + 1));

            // 将文件转换为 byte 数组
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while(-1 != (n = zis.read(buffer))) {
                output.write(buffer, 0, n);
            }

            imageDto.setBytes(output.toByteArray());
            dataList.add(imageDto);

        }
        zis.close();
        return dataList;
    }

    @Override
    public void compression(List<ImageDto> dataList, OutputStream os) throws IOException {

        if (CollectionUtils.isEmpty(dataList) || os == null){
            return;
        }

        // 把输出流包装为 压缩流
        ZipOutputStream zos = new ZipOutputStream(os);

        // 循环写压缩文件
        for (ImageDto file : dataList) {
            ZipEntry ze = new ZipEntry(file.getFileName());
            zos.putNextEntry(ze);
            zos.write(file.getBytes(),0,file.getBytes().length);
            zos.closeEntry();
        }
        zos.close();
    }


}
