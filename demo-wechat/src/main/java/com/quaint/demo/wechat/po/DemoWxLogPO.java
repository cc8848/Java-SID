package com.quaint.demo.wechat.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author quaint
 * @date 2020-01-15 13:45
 */
@TableName("demo_wx_log")
@Data
public class DemoWxLogPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 来源类型 （1.支付 2. 退款)
     */
    @TableField("source_type")
    private Integer sourceType;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 方向 (1.主动调用 2.回调通知)
     */
    private Integer direction;

    /**
     * 请求报文
     */
    @TableField("request_body_log")
    private String requestBodyLog;

    /**
     * 响应报文
     */
    @TableField("response_body_log")
    private String responseBodyLog;

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
}
