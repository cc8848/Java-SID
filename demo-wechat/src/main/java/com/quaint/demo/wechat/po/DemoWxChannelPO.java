package com.quaint.demo.wechat.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 微信渠道表
 * </p>
 *
 * @author quaint
 * @since 2020-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demo_wx_channel")
public class DemoWxChannelPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 渠道编码,1.demo 公众号渠道 2. demo 小程序渠道
     */
    private Integer channel;

    /**
     * 类型： 1.公众号 2.小程序
     */
    private Integer type;

    /**
     * 应用名称
     */
    @TableField("app_name")
    private String appName;

    /**
     * 公众号appid
     */
    @TableField("app_id")
    private String appId;

    /**
     * 公众号app密钥
     */
    @TableField("app_secret")
    private String appSecret;

    /**
     * token
     */
    private String token;

    /**
     * aes key
     */
    @TableField("aes_key")
    private String aesKey;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 是否有效 1:是 0:否
     */
    private Boolean valid;


}
