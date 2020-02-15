package com.quaint.demo.zip.design;

import com.quaint.demo.zip.dto.ImageDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * @author quaint
 * @date 15 February 2020
 * @since 1.30
 */
@Component
public class RarStrategy implements CompressionStrategy<ImageDto> {

    private static final String RAR_FORMAT = ".rar";

    @Override
    public boolean support(String fileName) {
        if (StringUtils.isEmpty(fileName)){
            return false;
        }
        return fileName.endsWith(RAR_FORMAT);
    }

    @Override
    public List<ImageDto> extract(InputStream inputStream) {
        return null;
    }

    @Override
    public InputStream compression(List<ImageDto> dataList) {
        return null;
    }
}
