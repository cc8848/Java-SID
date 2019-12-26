package com.quaint.demo.es.service.impl;

import com.quaint.demo.es.index.ItemIndex;
import com.quaint.demo.es.repository.ItemRepository;
import com.quaint.demo.es.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author qi cong
 * @date 2019-12-26 11:34
 */
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    ItemRepository itemRepository;


    @Override
    public Boolean esTemplateAddIndex() {

        // 可以根据类的信息自动生成，也可以手动指定indexName和Settings  elasticsearchTemplate.putMapping;
        elasticsearchTemplate.createIndex(ItemIndex.class);
        return true;
    }

    @Override
    public Boolean esRepositoryAddDocument() {

        ItemIndex itemIndex = new ItemIndex();
        itemIndex.setId(1L);
        itemIndex.setTitle("this is title");
        itemIndex.setCategory("typeOne");
        itemIndex.setScore(9.9);
        itemIndex.setPageViews(666L);
        itemIndex.setLike(true);
        itemIndex.setImages("image/url");
        itemIndex.setCreateTime(LocalDateTime.now());

        log.info("save : [{}]",itemIndex.toString());
        itemRepository.save(itemIndex);
        return true;
    }
}
