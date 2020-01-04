package com.quaint.demo.es.search.repository;

import com.quaint.demo.es.search.index.DemoTestIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * DemoTestIndex: 索引
 * Long: 索引主键的数据类型
 * @author qi cong
 * @date 2019-12-26 11:51
 */
public interface DemoTestRepository extends ElasticsearchRepository<DemoTestIndex,Long> {

}
