package com.quaint.demo.es.service;

import com.quaint.demo.es.dto.article.AddDemoArticleReqDto;
import com.quaint.demo.es.dto.article.DemoArticleDto;

/**
 * @author quaint
 * @date 2020-01-03 23:35
 */
public interface DemoArticleService {

    /**
     * 获取文章列表
     * @param param p
     * @return dto
     */
    DemoArticleDto getDemoArticleList(DemoArticleDto.Param param);

    /**
     * 增加文章到数据库,并更新索引
     * @param reqDto dto
     * @return boolean
     */
    Boolean addDemoArticle(AddDemoArticleReqDto reqDto);

}
