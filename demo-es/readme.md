### ES常用url

**ES 6-2-2下载地址**

    https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-2-2

**查看索引列表**

    // 索引列表
    http://localhost:9200/_cat/indices?v


**查看对应索引文档**

    // 查看指定索引,指定类型,指定id的内容, pretty(格式化json显示)
    http://localhost:9200/demo/item/1?pretty
    
    // 查看指定索引,指定类型 所有内容
    http://localhost:9200/demo/item/_search?pretty
    
    