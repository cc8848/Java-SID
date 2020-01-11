# ES入门 Demo


## DESC:

 - spring 整合 elasticsearch
 - spring 整合 mybatis-plus
 - 包含事件封装代码练习
 - demo_test 没有连接数据库
 - demo_article 连接了数据库
    

## 快速上手:


#### 准备:
```text
** ES 6-2-2下载地址 **
https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-2-2

** ik 分词器下载 **
https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v6.2.2
解压所有文件复制到elasticsearch的plugins目录下(可以在plugins下新建ik文件夹,放在里面方便更多插件整理)

** 最近参考的ES查询API **
https://docs.spring.io/spring-data/elasticsearch/docs/3.0.8.RELEASE/reference/html/#repositories

```


#### pom.xml

```xml
<!-- es 启动器, 7.0+ 需手动整合,配置 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```


#### application.yml

```yaml
spring:
  # 配置 es
  data:
    elasticsearch:
      cluster-name: my-application
      cluster-nodes: 127.0.0.1:9300
```


#### demo

配置类
```java
package com.quaint.demo.es.config;

@Configuration
public class ElasticSearchConfig {

    /**
     * 创建elasticsearchTemplate
     * @param client
     * @param converter
     * @return
     */
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client, ElasticsearchConverter converter){
        return new ElasticsearchTemplate(client, converter,new DefaultResultMapper(converter.getMappingContext(),new DemoEntityMapper()));
    }
}
```

索引
```java
package com.quaint.demo.es.search.index;

@Data
@Document(indexName = "demo_article")
public class DemoArticleIndex {

    @Id
    private Integer id;

    /**
     * 文章名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 内容
     */
    @Field(type = FieldType.Text)
    private String content;

    /**
     * 浏览量
     */
    @Field(type = FieldType.Integer)
    private Integer pageViews;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;

}
```

资料库
```java
package com.quaint.demo.es.search.repository;

public interface DemoArticleRepository extends ElasticsearchRepository<DemoArticleIndex,Integer> {
}
```

测试执行
```java
package com.quaint.demo.es.service.impl;

@Service
public class DemoArticleServiceImpl implements DemoArticleService {

    @Autowired
    DemoArticleRepository demoArticleRepository;


    @Override
    public DemoArticleDto getDemoArticleList(DemoArticleDto.Param param) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // bool 查询条件
        BoolQueryBuilder bool = new BoolQueryBuilder();
        if (StringUtils.isNotEmpty(param.getTitle())){
            bool.should(QueryBuilders.matchQuery("title",param.getTitle()));
        }
        if (StringUtils.isNotEmpty(param.getContent())){
            bool.should(QueryBuilders.matchQuery("content",param.getContent()));
        }

        // 过滤作品浏览数 大于1的
        bool.filter(QueryBuilders.rangeQuery("pageViews").gt(1));

        queryBuilder.withQuery(bool);

        // 数据排序, 随机排序, 随机后根据时间降序
        queryBuilder.withSort(SortBuilders.scriptSort(new Script("Math.random()"), ScriptSortBuilder.ScriptSortType.NUMBER));
        queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));

        // 数据设置分页, 不设置默认为10条数据
        if (param.esStandardization()){
            queryBuilder.withPageable(PageRequest.of(param.getPageNum(),param.getPageSize()));
        }

        // 查询
        Page<DemoArticleIndex> search = demoArticleRepository.search(queryBuilder.build());
        return processIndex2Dto(search);
    }
}
```

本次测试 可能会用到的 url 命令
```text
** 测试使用到的url命令 **
// GET 索引列表
http://localhost:9200/_cat/indices?v

// GET 查看指定索引,指定类型,指定id的内容, pretty(格式化json显示)
http://localhost:9200/demo/item/1?pretty

// GET 查看指定索引,指定类型 所有内容
http://localhost:9200/demo/item/_search?pretty

// DELETE 删除对应索引
http://localhost:9200/demo
```

    