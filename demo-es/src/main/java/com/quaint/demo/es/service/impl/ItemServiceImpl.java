package com.quaint.demo.es.service.impl;

import com.quaint.demo.es.dto.ItemDTO;
import com.quaint.demo.es.index.ItemIndex;
import com.quaint.demo.es.repository.ItemRepository;
import com.quaint.demo.es.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        if (id.intValue()%titleCount==0){
            itemIndex.setTitle("广寒宫,饭思思,歌曲");
        } else if (id.intValue()%titleCount==1){
            itemIndex.setTitle("你的酒馆对我打了烊,陈雪凝,歌曲");
        } else {
            itemIndex.setTitle("清明上河图,洛天依,歌曲");
        }
        itemIndex.setCategory(id%2>0?"类型一":"类型二");
        itemIndex.setScore(9.9*id.intValue());
        itemIndex.setPageViews(666L+id);
        itemIndex.setLike(id.intValue()%2>0);
        itemIndex.setImages("image/url/"+id);
        // 控制一部分时间相同
        itemIndex.setCreateTime(LocalDateTime.now().withHour(id.intValue()%3+10).withNano(0));
        return itemIndex;
    }

    @Override
    public ItemDTO searchItemList(ItemDTO.Param param) {

        if (param != null) {

            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

            // 增加搜索条件
            BoolQueryBuilder boolQuery = new BoolQueryBuilder();
            if (param.getTitle()!=null) {
                boolQuery.should(QueryBuilders.matchQuery("title",param.getTitle()));
            }
            if (!CollectionUtils.isEmpty(param.getCategory())) {
                param.getCategory().forEach(category -> boolQuery.should(QueryBuilders.matchQuery("category",category)));
            }
            queryBuilder.withQuery(boolQuery);

            // 处理分页
            if (param.esStandardization()){
                queryBuilder.withPageable(PageRequest.of(param.getPageNum(),param.getPageSize()));
            } else {
                // 如果没有传分页参数,则为查询全部,es并不推荐直接查询全部,这里暂时插叙1000条
                queryBuilder.withPageable(PageRequest.of(0,1000));
            }

            // 结果排序
            queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
            // 如果 时间相同,会根据 pageViews 升序排序
            queryBuilder.withSort(SortBuilders.fieldSort("pageViews").order(SortOrder.ASC));

            // 开始查询
            Page<ItemIndex> searchPage = itemRepository.search(queryBuilder.build());

            // 封装dto
            ItemDTO respDto = new ItemDTO();
            respDto.setTotalCount(searchPage.getTotalElements());
            List<ItemDTO.Result> resultList = searchPage.getContent().stream().map(itemIndex -> {
                ItemDTO.Result result = new ItemDTO.Result();
                BeanUtils.copyProperties(itemIndex, result);
                return result;
            }).collect(Collectors.toList());
            respDto.setItemList(resultList);

            return respDto;
        }
        return null;
    }
}
