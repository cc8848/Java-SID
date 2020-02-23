package com.quaint.common.image.abst;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 海报绘制
 * @author quaint
 * @date 21 February 2020
 * @since master
 */
public interface PosterDraw<T>{


    /**
     * 是否支持
     * @param clazz class
     * @return bool
     */
    boolean support(Class<?> clazz);

    /**
     * 通过数据绘制 并返回 图片
     * @param data 海报所需的数据
     * @return 图片
     * @throws IOException io
     */
    BufferedImage drawAndReturnImage(T data) throws IOException;

}
