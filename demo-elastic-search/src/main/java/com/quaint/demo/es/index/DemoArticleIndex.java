package com.quaint.demo.es.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * @author quaint
 * @date 2020-01-04 12:54
 */
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
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;

}
