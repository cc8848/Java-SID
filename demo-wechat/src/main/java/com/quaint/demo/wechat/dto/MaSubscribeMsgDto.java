package com.quaint.demo.wechat.dto;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import com.quaint.demo.wechat.config.helper.WxServiceContainer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 订阅消息 dto
 * @author quaint
 * @date 2020-01-07 12:53
 */
@Data
public class MaSubscribeMsgDto {

    /**
     * @see WxServiceContainer
     */
    @ApiModelProperty("渠道")
    private Integer channel;

    @ApiModelProperty(value = "模板Id",required = true)
    @NotNull(message = "模板不能为空")
    private String templateId;

    @ApiModelProperty(value = "用户openId",required = true)
    @NotNull(message = "用户openId不能为空")
    private String openId;

    @ApiModelProperty(value = "小程序page",required = true)
    private String page;

    @ApiModelProperty(value = "模板数据",required = true)
    private List<WxMaTemplateData> data;

    public void appendData(String name,String value){
        if(this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.add(new WxMaTemplateData(name,value));
    }

}
