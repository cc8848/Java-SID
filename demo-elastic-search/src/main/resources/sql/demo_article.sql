CREATE TABLE `demo_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) DEFAULT NULL COMMENT '文章名称',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `page_views` int(11) DEFAULT '0' COMMENT '浏览量',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

INSERT INTO `javasid`.`demo_article`(`id`, `title`, `content`, `create_time`) VALUES (1, '米哈游', '技术宅拯救世界', '2020-01-04 14:16:51');
INSERT INTO `javasid`.`demo_article`(`id`, `title`, `content`, `create_time`) VALUES (2, '卡卡rot', '超级赛亚人~', '2020-01-04 14:16:55');
INSERT INTO `javasid`.`demo_article`(`id`, `title`, `content`, `create_time`) VALUES (3, '路飞', '成为海贼王的男人', '2020-01-04 14:16:59');
INSERT INTO `javasid`.`demo_article`(`id`, `title`, `content`, `create_time`) VALUES (4, '程序员', '我秃了,也变强了', '2020-01-04 14:17:03');
INSERT INTO `javasid`.`demo_article`(`id`, `title`, `content`, `create_time`) VALUES (5, '大龄程序员', '我秃了,也没工作了..', '2020-01-04 14:17:59');
INSERT INTO `javasid`.`demo_article`(`id`, `title`, `content`, `create_time`) VALUES (6, '前端程序员', '没有ui稿就没有排期,哼!!!', '2020-01-04 14:19:05');