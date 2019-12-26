package com.quaint.demo.mail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qi cong
 * @date 2019-12-25 19:11
 */
@ApiModel("发送邮件dto")
@Data
public class SendMailDto {

    @ApiModelProperty("发送的标题")
    private String title;

    @ApiModelProperty("发送的内容")
    private String content;

    @ApiModelProperty("发送人的mail")
    private String toMail;

}
