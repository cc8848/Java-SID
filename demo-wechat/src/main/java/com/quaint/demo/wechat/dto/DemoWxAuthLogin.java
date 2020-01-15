package com.quaint.demo.wechat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author quaint
 * @date 2020-01-08 10:11
 */
public interface DemoWxAuthLogin {

    @Data
    @ApiModel("授权登录请求体")
    class Param{

        @ApiModelProperty(value = "用户授权code",required = true)
        @NotNull(message = "授权code不能为空")
        private String code;

        @ApiModelProperty(value = "消息密文",required = true)
        private String encryptedData;

        @ApiModelProperty(value = "加密算法的初始向量",required = true)
        private String ivStr;

    }

    @Data
    @ApiModel("授权登录返回凭证")
    class Result <T>{

        @ApiModelProperty("登录凭证")
        T token;
    }

}
