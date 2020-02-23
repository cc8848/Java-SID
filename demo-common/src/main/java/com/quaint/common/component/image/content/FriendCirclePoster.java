package com.quaint.common.component.image.content;

import com.quaint.common.component.image.abst.AbstractPoster;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 朋友圈海报
 * @author quaint
 * @date 21 February 2020
 * @since master
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FriendCirclePoster extends AbstractPoster {

    /**
     * 商品图
     */
    private List<BufferedImage> prodImages;

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
    public FriendCirclePoster(BufferedImage backgroundImage, BufferedImage logo, String slogan, BufferedImage mainImage, BufferedImage qrcode, List<BufferedImage> prodImages, BufferedImage headImage, String userNickName, String priceRange, String linePrice) {
        super(backgroundImage, logo, slogan, mainImage, qrcode);
        this.prodImages = prodImages;
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
