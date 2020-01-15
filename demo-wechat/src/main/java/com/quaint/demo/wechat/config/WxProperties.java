package com.quaint.demo.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author quaint
 * @date 2020-01-12 13:48
 */
@ConfigurationProperties(prefix = "wx")
@Data
public class WxProperties {

    @NestedConfigurationProperty
    private MaProperties ma;

    @NestedConfigurationProperty
    private MiniAppProperties miniApp;

    @NestedConfigurationProperty
    private PayProperties pay;



    @Data
    public static class MaProperties{

        private String appId;
        private String appSecret;
        private String token;
        private String aesKey;

    }

    @Data
    public static class MiniAppProperties{

        private String appId;
        private String appSecret;
    }

    @Data
    public static class PayProperties{

        /**
         * 微信公众号或者小程序等的appid
         */
        private String appId;
        /**
         * 微信支付商户号
         */
        private String mchId;
        /**
         * 微信支付商户密钥
         */
        private String mchKey;

        /**
         * 回调服务
         */
        private String notifyServer;

        /**
         * p12证书的位置，可以指定绝对路径，也可以指定类路径（以classpath:开头）
         */
        private String keyPath;

        /**
         * 获取支付通知url
         * @return url
         */
        public String getPayNotifyUrl(){
            return getNotifyServer() + "/wx/pay/pay_notify";
        }

        /**
         * 获取退款通知url
         * @return url
         */
        public String getRefundNotifyUrl(){
            return getNotifyServer() + "/wx/pay/refund_notify";
        }
    }

}
