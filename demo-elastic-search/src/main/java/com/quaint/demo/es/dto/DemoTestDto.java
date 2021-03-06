package com.quaint.demo.es.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quaint.demo.es.dto.base.BasePageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author quaint
 * @date 2019-12-26 17:43
 */
@ApiModel("ItemDto")
@Data
public class DemoTestDto {

    @ApiModelProperty("item列表")
    private List<Result> itemList;

    @ApiModelProperty("item总数")
    private Long totalCount;


    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Param extends BasePageDto {

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

        @ApiModelProperty("创建时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTime;
    }

}
