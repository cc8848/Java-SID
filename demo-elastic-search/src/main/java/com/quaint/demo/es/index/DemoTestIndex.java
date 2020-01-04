package com.quaint.demo.es.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * Desc: es 索引定义
 * @author qi cong
 * @date 2019-12-26 11:15
 */
@Document(indexName = "demo_test")
@Data
public class DemoTestIndex {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 标题 ik 分词器
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 分类 逗号分隔
     */
    @Field(type = FieldType.Text)
    private String category;

    /**
     * 评分
     */
    @Field(type = FieldType.Double, index = false)
    private Double score;

    /**
     * 浏览量
     */
    @Field(type = FieldType.Long, index = false)
    private Long pageViews;

    /**
     * 是否喜欢
     */
    @Field(type = FieldType.Boolean, index = false)
    private Boolean like;

    /**
     * 图片地址
     */
    @Field(type = FieldType.Text, index = false)
    private String images;

    /**
     * 创建日期
     */
    @Field(type = FieldType.Date, index = false)
    private LocalDateTime createTime;

}
