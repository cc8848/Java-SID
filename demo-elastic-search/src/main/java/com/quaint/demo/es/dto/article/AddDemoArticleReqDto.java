package com.quaint.demo.es.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author quaint
 * @date 2020-01-04 16:10
 */
@Data
@ApiModel("增加文章请求dto")
public class AddDemoArticleReqDto {

    @NotNull(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @NotNull(message = "内容不能为空")
    @ApiModelProperty("内容")
    private String content;

}
