package com.quaint.zip.dto;

import lombok.Data;

/**
 * 图片实体类,简单版
 * @author quaint
 * @date 15 February 2020
 * @since 1.30
 */
@Data
public class ImageDto {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件字节码
     */
    private byte[] bytes;

}
