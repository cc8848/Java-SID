package com.quaint.demo.es.search.repository;

import com.quaint.demo.es.search.index.DemoArticleIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author quaint
 * @date 2020-01-04 12:59
 */
public interface DemoArticleRepository extends ElasticsearchRepository<DemoArticleIndex,Integer> {
}
