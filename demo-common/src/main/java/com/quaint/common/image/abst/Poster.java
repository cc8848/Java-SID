package com.quaint.common.image.abst;

import java.awt.image.BufferedImage;

/**
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
