package com.quaint.demo.es.service;

import com.quaint.demo.es.dto.article.DemoArticleDto;

/**
 * @author quaint
 * @date 2020-01-03 23:35
 */
public interface DemoArticleService {

    DemoArticleDto getDemoArticleList(DemoArticleDto.Param param);

}
