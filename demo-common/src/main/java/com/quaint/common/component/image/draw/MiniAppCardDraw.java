package com.quaint.common.component.image.draw;

import com.quaint.common.component.image.abst.PosterDraw;
import com.quaint.common.component.image.content.MiniAppCardPoster;
import com.quaint.common.component.image.decorators.BackgroundDecorator;
import com.quaint.common.component.image.decorators.ImageDecorator;
import com.quaint.common.component.image.decorators.TextDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author quaint
 * @date 21 February 2020
 * @since 1.34
 */
@Component
@Slf4j
public class MiniAppCardDraw implements PosterDraw<MiniAppCardPoster> {


    @Override
    public boolean support(Class<?> clazz) {
        if (clazz == null){
            return false;
        }
        return clazz.equals(MiniAppCardPoster.class);
    }

    @Override
    public BufferedImage drawAndReturnImage(MiniAppCardPoster poster) throws IOException{
        log.info("[drawAndReturnImage] method start, param:[{}]", poster);
        // ======= 主逻辑开始 --> =======

        // 1. 绘制背景 最好取背景的 width 和 height
        BackgroundDecorator drawBg = new BackgroundDecorator(poster).toBuilder()
                .width(420).height(336).build();
        // 2. 绘制头像
        ImageDecorator drawHead = new ImageDecorator(drawBg).toBuilder()
                .positionX(27).positionY(27)
                .width(36).height(36)
                .circle(true)
                .image(poster.getHeadImage()).build();
        // 3. 绘制昵称
        TextDecorator drawNickName = new TextDecorator(drawHead).toBuilder()
                .positionX(71).positionY(32)
                .fontSize(18)
                .content(poster.getUserNickName()+" 向你推荐").build();
        // 3. 绘制商品介绍
        TextDecorator drawSlogan = new TextDecorator(drawNickName).toBuilder()
                .positionX(27).positionY(70)
                .fontSize(22).fontStyle(Font.BOLD)
                .content(poster.getSlogan()).build();
        // 4. 绘制商品图片
        ImageDecorator drawProdImg = new ImageDecorator(drawSlogan).toBuilder()
                .positionX(24).positionY(129)
                .width(168).height(168)
                .image(poster.getMainImage()).build();
        // 5. 绘制价格返回
        TextDecorator drawPriceRange = new TextDecorator(drawProdImg).toBuilder()
                .positionX(203).positionY(155)
                .fontSize(24).fontStyle(Font.BOLD)
                .color(new Color(216,11,42))
                .content(poster.getPriceRange()).build();
        // 6. 绘制删除线价格
        TextDecorator drawLinePrice = new TextDecorator(drawPriceRange).toBuilder()
                .positionX(240).positionY(187)
                .fontSize(18).delLine(true)
                .color(new Color(153,153,153))
                .content(poster.getLinePrice()).build();
        // 调用最后一个包装类的 draw 方法
        BufferedImage drawResult = drawLinePrice.draw(poster.getBackgroundImage());

        // ======= <-- 主逻辑结束 =======
        log.info("[drawAndReturnImage] method end, result:[success]");
        return drawResult;
    }
}
