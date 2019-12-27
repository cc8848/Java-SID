package com.quaint.demo.es.dto;

import com.quaint.demo.es.dto.base.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author qi cong
 * @date 2019-12-26 17:43
 */
@ApiModel("ItemDto")
@Data
public class ItemDTO {

    @ApiModelProperty("item列表")
    private List<Result> itemList;

    @ApiModelProperty("item总数")
    private Long totalCount;


    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Param extends BasePage {

        @ApiModelProperty("标题")
        private String title;

        @ApiModelProperty("分类,逗号分隔")
        private List<String> category;

    }

    @Data
    public static class Result{

        private Long id;

        @ApiModelProperty("标题")
        private String title;

        @ApiModelProperty("分类")
        private String category;

        @ApiModelProperty("评分")
        private Double score;

        @ApiModelProperty("浏览量")
        private Long pageViews;

        @ApiModelProperty("图片地址")
        private String images;
    }

}
