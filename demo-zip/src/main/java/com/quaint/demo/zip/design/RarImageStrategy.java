package com.quaint.demo.zip.design;

import com.quaint.demo.zip.dto.ImageDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author quaint
 * @date 15 February 2020
 * @since 1.30
 */
@Component
public class RarImageStrategy implements CompressionStrategy<ImageDto> {

    /**
     * 传入文件类型
     */
    private static final String RAR_FORMAT = ".rar";

    /**
     * 目标类型
     */
    private static final List<String> TARGET_TYPE = Arrays.asList(".png", ".jpeg", ".jpg", ".gif");

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
    public void compression(List<ImageDto> dataList, OutputStream os) {

    }
}
