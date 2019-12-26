package com.quaint.demo.es.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qi cong
 * @date 2019-12-26 11:53
 */
@Data
public class Item {

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类
     */
    private String category;

    /**
     * 评分
     */
    private Double score;

    /**
     * 浏览量
     */
    private Long pageViews;

    /**
     * 图片地址
     */
    private String images;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

}
