package com.quaint.demo.es.service;

import com.quaint.demo.es.dto.article.AddDemoArticleReqDto;
import com.quaint.demo.es.dto.article.DemoArticleDto;
import com.quaint.demo.es.dto.base.BaseIdDto;

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

    /**
     * 通过id查询文章
     * @param id id
     * @return 文章
     */
    DemoArticleDto.Result getDemoArticleById(Integer id);

}
