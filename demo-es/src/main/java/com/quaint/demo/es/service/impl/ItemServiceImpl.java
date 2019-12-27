package com.quaint.demo.es.service.impl;

import com.quaint.demo.es.dto.ItemDTO;
import com.quaint.demo.es.index.ItemIndex;
import com.quaint.demo.es.repository.ItemRepository;
import com.quaint.demo.es.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    public Boolean esRepositoryAddDocument(ItemDTO dto) {

        ItemIndex itemIndex = new ItemIndex();
        BeanUtils.copyProperties(dto,itemIndex);
        itemIndex.setLike(false);
        itemIndex.setCreateTime(LocalDateTime.now());

        log.info("save : [{}]",itemIndex.toString());
        itemRepository.save(itemIndex);
        return true;
    }

    @Override
    public Boolean initIndexData() {
        ItemIndex itemIndex;
        Long count = 20L;
        for (Long i = 0L; i < count; i++) {
            itemIndex = createItemIndex(i);
            itemRepository.save(itemIndex);
        }
        return true;
    }

    /**
     * 创建数据
     * @return
     */
    private ItemIndex createItemIndex(Long id){
        ItemIndex itemIndex = new ItemIndex();
        itemIndex.setId(id);
        int titleCount = 3;
        if (id%titleCount==0){
            itemIndex.setTitle("广寒宫,饭思思,歌曲");
        } else if (id%titleCount==1){
            itemIndex.setTitle("你的酒馆对我打了烊,陈雪凝,歌曲");
        } else {
            itemIndex.setTitle("清明上河图,洛天依,歌曲");
        }
        itemIndex.setCategory(id%2>0?"类型一":"类型二");
        itemIndex.setScore(9.9*id);
        itemIndex.setPageViews(666L+id);
        itemIndex.setLike(id%2>0);
        itemIndex.setImages("image/url/"+id);
        return itemIndex;
    }

}
