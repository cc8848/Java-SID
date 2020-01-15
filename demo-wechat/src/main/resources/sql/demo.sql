CREATE TABLE `demo_wx_channel` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel` tinyint(3) NOT NULL COMMENT '渠道编码,1.demo公众号渠道 2.demo小程序渠道',
  `type` tinyint(3) NOT NULL COMMENT '类型： 1.公众号 2.小程序 ',
  `app_name` varchar(15) NOT NULL COMMENT '应用名称',
  `app_id` varchar(30) NOT NULL COMMENT '公众号appid',
  `app_secret` varchar(35) NOT NULL COMMENT '公众号app密钥',
  `token` varchar(35) DEFAULT NULL COMMENT 'token',
  `aes_key` varchar(50) DEFAULT NULL COMMENT 'aes key',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `valid` tinyint(1) NOT NULL COMMENT '是否有效 1:是 0:否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='微信渠道表';

CREATE TABLE `demo_wx_log` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_type` tinyint(3) NOT NULL DEFAULT '1' COMMENT '来源类型 （1.支付 2. 退款)',
  `source_id` bigint(19) NOT NULL COMMENT '来源id',
  `direction` tinyint(3) NOT NULL DEFAULT '1' COMMENT '方向 (1.主动调用 2.回调通知)',
  `request_body_log` text COMMENT '请求报文',
  `response_body_log` text COMMENT '响应报文',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_source_type_id` (`source_type`,`source_id`)
) ENGINE=InnoDB COMMENT='微信日志';

CREATE TABLE `demo_wx_pay_order` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_type` tinyint(3) NOT NULL COMMENT '订单类型：1.美力联盟活动',
  `order_code` varchar(50) NOT NULL COMMENT '订单号',
  `body` varchar(100) NOT NULL COMMENT '商品简单描述',
  `amount` decimal(22,6) NOT NULL DEFAULT '0.000000' COMMENT '金额',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态：0.草稿 10.待支付 20.支付成功 30.支付失败',
  `prepayid` varchar(50) NOT NULL DEFAULT '' COMMENT '预支付id',
  `prepay_result` mediumtext NOT NULL COMMENT '预支付响应报文',
  `transaction_id` varchar(50) NOT NULL DEFAULT '' COMMENT '微信订单号',
  `success_time` datetime DEFAULT NULL COMMENT '成功时间',
  `expiration_time` datetime DEFAULT NULL COMMENT '失效时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效：1.是 0.否',
  PRIMARY KEY (`id`),
  KEY `idx_order_code` (`order_code`)
) ENGINE=InnoDB COMMENT='微信支付单';

CREATE TABLE `demo_wx_refund_order` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `wx_pay_order_id` bigint(19) NOT NULL COMMENT '微信支付单id',
  `refund_order_code` varchar(50) NOT NULL COMMENT '退款单号',
  `refund_desc` varchar(50) NOT NULL COMMENT '退款描述',
  `amount` decimal(22,6) NOT NULL DEFAULT '0.000000' COMMENT '金额',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态：10.退款中 20.退款成功 30.退款失败',
  `transaction_id` varchar(50) NOT NULL DEFAULT '' COMMENT '微信退款单号',
  `success_time` datetime DEFAULT NULL COMMENT '成功时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效：1.是 0.否',
  PRIMARY KEY (`id`),
  KEY `idx_refund_order_code` (`refund_order_code`),
  KEY `idx_transaction_id` (`transaction_id`)
) ENGINE=InnoDB COMMENT='微信退款单';