package com.quaint.common.spi;

import com.quaint.common.image.abst.PosterDraw;
import com.quaint.common.image.content.MiniAppCardPoster;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author quaint
 * @date 23 February 2020
 * @since master
 */
@RestController
@Api(tags = {"图片测试api", "分类: 公共图片"})
@Slf4j
public class ImageDemoSpi {

    @Autowired
    List<PosterDraw> posterDraws;

    @ApiOperation("创建小程序卡片测试")
    @PostMapping("/mini/app/card")
    public String createMiniAppCardTest() {
        log.info("[createMiniAppCardTest] method start");
        String result = null;
        // ======= 主逻辑开始 --> =======

        try {
            // 获取当前项目文件夹的的zip文件
            String filePath = System.getProperty("user.dir") + "/demo-common/src/main/resources/";
            BufferedImage bgImage = ImageIO.read(new FileInputStream(filePath + "image/bg01.jpg"));
            BufferedImage mainImage = ImageIO.read(new FileInputStream(filePath + "image/main.jpg"));
            BufferedImage headImage = ImageIO.read(new FileInputStream(filePath + "image/head.jpeg"));

            // 创建小程序卡片测试
            MiniAppCardPoster poster = new MiniAppCardPoster().toBuilder()
                    .backgroundImage(bgImage)
                    .mainImage(mainImage)
                    .headImage(headImage)
                    .userNickName("Quaint")
                    .slogan("技术宅拯救世界")
                    .priceRange("￥70.00-100.00")
                    .linePrice("原价:100.00").build();

            // 选取绘制海报策略
            Optional<PosterDraw> posterDraw = posterDraws.stream().filter(d -> d.support(MiniAppCardPoster.class)).findFirst();
            if (posterDraw.isPresent()) {
                BufferedImage image = posterDraw.get().drawAndReturnImage(poster);
                // 本地测试效果
                ImageIO.write(image, "png", new FileOutputStream("drawCardTest.png"));
                result = "success";
            }

        } catch (IOException e) {
            e.printStackTrace();
            result = "fail";
        }

        // ======= <-- 主逻辑结束 =======
        log.info("[createMiniAppCardTest] method end, result:[{}]", result);
        return result;

    }


}
