package com.quaint.demo.es.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qi cong
 * @date 2019-12-26 17:43
 */
@ApiModel("ItemDto")
@Data
public class ItemDTO {

    private Long id;

    @ApiModelProperty("标题")
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


}
