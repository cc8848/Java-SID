package com.quaint.demo.es.repository;

import com.quaint.demo.es.index.ItemIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * ItemIndex: 索引
 * Long: 索引主键的数据类型
 * @author qi cong
 * @date 2019-12-26 11:51
 */
public interface ItemRepository extends ElasticsearchRepository<ItemIndex,Long> {

}
