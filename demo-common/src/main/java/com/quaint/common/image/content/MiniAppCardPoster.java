package com.quaint.common.image.content;

import com.quaint.common.image.abst.AbstractPoster;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

/**
 * 朋友小卡片海报
 * @author quaint
 * @date 21 February 2020
 * @since master
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MiniAppCardPoster extends AbstractPoster {

    /**
     * 用户头像
     */
    private BufferedImage headImage;

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 价格范围
     */
    private String priceRange;

    /**
     * 划线价
     */
    private String linePrice;


    @Builder(toBuilder = true)
    public MiniAppCardPoster(BufferedImage backgroundImage, BufferedImage logo, String slogan, BufferedImage mainImage, BufferedImage qrcode, BufferedImage headImage, String userNickName, String priceRange, String linePrice) {
        super(backgroundImage, logo, slogan, mainImage, qrcode);
        this.headImage = headImage;
        this.userNickName = userNickName;
        this.linePrice = linePrice;
        this.priceRange = priceRange;
    }

    @Override
    public BufferedImage draw(BufferedImage image) {
        return image;
    }

}
