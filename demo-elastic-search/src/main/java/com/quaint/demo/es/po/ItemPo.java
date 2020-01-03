package com.quaint.demo.es.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Desc: item po 虚拟po
 * @author qi cong
 * @date 2019-12-26 11:15
 */
@Data
public class ItemPo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类 逗号分隔
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
     * 是否喜欢
     */
    private Boolean like;

    /**
     * 图片地址
     */
    private String images;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

}
