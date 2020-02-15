package com.quaint.demo.zip.design;

import com.quaint.demo.zip.dto.ImageDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author quaint
 * @date 15 February 2020
 * @since 1.30
 */
@Component
public class ZipStrategy implements CompressionStrategy<ImageDto> {

    private static final String ZIP_FORMAT = ".zip";

    @Override
    public boolean support(String fileName) {
        if (StringUtils.isEmpty(fileName)){
            return false;
        }
        return fileName.endsWith(ZIP_FORMAT);
    }

    @Override
    public List<ImageDto> extract(InputStream inputStream) {

        // 储存数据的list
        List<ImageDto> dataList = new ArrayList<>();

        ZipInputStream zis = new ZipInputStream(inputStream);

        System.out.println(zis);
        return null;
    }

    @Override
    public InputStream compression(List<ImageDto> dataList) {
        return null;
    }


}
