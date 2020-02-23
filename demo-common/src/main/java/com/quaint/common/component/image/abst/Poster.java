package com.quaint.common.component.image.abst;

import java.awt.image.BufferedImage;

/**
 * 海报接口定义
 * @author quaint
 * @date 21 February 2020
 * @since master
 */
public interface Poster {

    /**
     * 画海报
     * @param image image
     * @return image
     */
    BufferedImage draw(BufferedImage image);

}
