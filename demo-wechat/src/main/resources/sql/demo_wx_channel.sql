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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信渠道表';