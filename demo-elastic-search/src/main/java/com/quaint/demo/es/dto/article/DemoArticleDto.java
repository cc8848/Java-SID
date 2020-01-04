package com.quaint.demo.es.dto.article;

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
 * @date 2020-01-04 13:48
 */
@Data
public class DemoArticleDto{

    private List<Result> articleList;

    private Long total;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @ApiModel("文章请求参数")
    public static class Param  extends BasePageDto {

        @ApiModelProperty("标题")
        private String title;

        @ApiModelProperty("内容")
        private String content;

    }

    @Data
    @ApiModel("文章返回参数")
    public static class Result{

        private Integer id;

        @ApiModelProperty("标题")
        private String title;

        @ApiModelProperty("内容")
        private String content;

        @ApiModelProperty("浏览量")
        private Integer pageViews;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @ApiModelProperty("创建时间")
        private LocalDateTime createTime;

    }


}
