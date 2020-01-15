package com.quaint.demo.wechat.dto;

import com.quaint.demo.wechat.helper.WxServiceContainer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 公众号模板消息, 2020-01-10 没有影响, 可以继续使用
 * @author quaint
 * @date 2020-01-06 12:42
 */
@ApiModel("发送模板消息请求dto")
@Data
public class MpTemplateMsgDto {

    /**
     * @see WxServiceContainer
     */
    @ApiModelProperty("渠道-多渠道时 使用; demo 无需使用")
    @Deprecated
    private Integer channel;

    @ApiModelProperty(value = "模板Id",required = true)
    @NotNull(message = "模板不能为空")
    private String templateId;

    @ApiModelProperty(value = "用户openId",required = true)
    @NotNull(message = "用户openId不能为空")
    private String openId;

    @ApiModelProperty(value = "消息点击跳转地址",required = true)
    private String url;

    @ApiModelProperty(value = "模板数据",required = true)
    private List<WxMpTemplateData> data;

    public void appendData(String name,String value){
        if(this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.add(new WxMpTemplateData(name,value));
    }

}
