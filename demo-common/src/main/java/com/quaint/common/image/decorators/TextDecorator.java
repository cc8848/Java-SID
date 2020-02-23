package com.quaint.common.image.decorators;

import com.quaint.common.image.abst.AbstractPosterDecorator;
import com.quaint.common.image.abst.Poster;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 绘制文本
 * @author quaint
 * @date 21 February 2020
 * @since master
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TextDecorator extends AbstractPosterDecorator {

    /**
     * 字体
     */
    private Font font = new Font(null);

    /**
     * 字体样式
     */
    private int fontStyle = Font.PLAIN;

    /**
     * 字体大小
     */
    private int fontSize = 16;

    /**
     * 字体颜色
     */
    private Color color = new Color(255,255,255);

    /**
     * 内容
     */
    private String content;

    /**
     * 是否包含删除先
     */
    private boolean delLine = false;


    public TextDecorator(Poster poster) {
        super(poster);
    }

    @Builder(toBuilder = true)
    public TextDecorator(Poster poster, int positionX, int positionY, int width, int height, Font font, int fontSize, Color color, String content, int fontStyle, boolean delLine) {
        super(poster,positionX,positionY,width,height);
        this.font = font;
        this.fontSize = fontSize;
        this.color = color;
        this.content = content;
        this.fontStyle = fontStyle;
        this.delLine = delLine;
    }

    @Override
    public BufferedImage draw(BufferedImage image) {
        // 绘制 被装饰之前的 图片
        BufferedImage draw = poster.draw(image);
        // 装饰, 绘制文本
        return drawText(draw);
    }

    /**
     * 绘制文本具体实现
     * @param image image
     * @return image
     */
    private BufferedImage drawText(BufferedImage image){

        if (StringUtils.isEmpty(content)){
            return image;
        }

        // 实现绘制文本
        font = font.deriveFont(fontStyle, fontSize);
        Graphics2D g = image.createGraphics();
        // 设置文字抗锯齿算法
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(color);
        g.drawString(content, positionX, positionY+fontSize);
        if (delLine){
            // 计算非汉字长度
            int shortNum = content.replaceAll("[^0-9,a-z,A-Z,.]", "").length();
            // 汉字长度
            int longNum = content.length()-shortNum;
            // 删除线长度 = (汉字长度 * size) + ((字符长度+1) * size/2)
            int num = longNum + (shortNum+1)/2;
            g.drawLine(positionX-fontSize/3,positionY+3*fontSize/5,positionX+fontSize*num,positionY+3*fontSize/5);
        }
        g.dispose();
        return image;
    }

}
