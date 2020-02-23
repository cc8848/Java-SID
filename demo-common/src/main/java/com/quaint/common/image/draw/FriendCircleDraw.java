package com.quaint.common.image.draw;

import com.quaint.common.image.abst.PosterDraw;
import com.quaint.common.image.content.FriendCirclePoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author quaint
 * @date 21 February 2020
 * @since master
 */
@Component
@Slf4j
public class FriendCircleDraw implements PosterDraw<FriendCirclePoster> {

    @Override
    public boolean support(Class<?> clazz) {
        if (clazz == null){
            return false;
        }
        return clazz.equals(FriendCirclePoster.class);
    }

    @Override
    public BufferedImage drawAndReturnImage(FriendCirclePoster data) throws IOException{
        log.info("[drawAndReturnImage] method start, param:[{}]", data);
        // ======= 主逻辑开始 --> =======


        // ======= <-- 主逻辑结束 =======
        log.info("[drawAndReturnImage] method end, result:[success]");
        return null;
    }
}
