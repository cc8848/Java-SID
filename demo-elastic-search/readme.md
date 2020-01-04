## ES入门 Demo

**demo简介**

    1. spring 整合 elasticsearch
    2. spring 整合 mybatis-plus
    3. 包含事件封装代码练习
    

**ES 6-2-2下载地址**

    https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-2-2

**ik 分词器下载**

    https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v6.2.2
    解压所有文件复制到elasticsearch的plugins目录下(可以在plugins下新建ik文件夹,放在里面方便更多插件整理)

**ES查询API**

    https://docs.spring.io/spring-data/elasticsearch/docs/3.0.8.RELEASE/reference/html/#repositories


**ES常见Url**

    // GET 索引列表
    http://localhost:9200/_cat/indices?v

    // GET 查看指定索引,指定类型,指定id的内容, pretty(格式化json显示)
    http://localhost:9200/demo/item/1?pretty
    
    // GET 查看指定索引,指定类型 所有内容
    http://localhost:9200/demo/item/_search?pretty

    // DELETE 删除对应索引
    http://localhost:9200/demo