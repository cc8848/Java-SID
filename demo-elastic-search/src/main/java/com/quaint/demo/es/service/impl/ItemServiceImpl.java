package com.quaint.demo.es.service.impl;

import com.quaint.demo.es.dto.ItemDTO;
import com.quaint.demo.es.handler.impl.ItemHandler;
import com.quaint.demo.es.index.ItemIndex;
import com.quaint.demo.es.mapper.ItemMapper;
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

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemHandler itemHandler;


    @Override
    public Boolean addIndex() {
        // 可以根据类的信息在项目启动时会自动生成，也可以手动指定indexName和Settings -->  elasticsearchTemplate.putMapping;
        // 一版情况下使用不到
        elasticsearchTemplate.createIndex(ItemIndex.class);
        return true;
    }

    @Override
    public Boolean initIndexData() {
        itemHandler.refresh();
        return true;
    }

    @Override
    public Boolean addDocument(ItemDTO dto) {

        ItemIndex itemIndex = new ItemIndex();
        BeanUtils.copyProperties(dto,itemIndex);
        itemIndex.setLike(false);
        itemIndex.setCreateTime(LocalDateTime.now());

        log.info("save : [{}]",itemIndex.toString());
        itemRepository.save(itemIndex);
        return true;
    }

    @Override
    public Boolean delDocumentById(Long id) {
        itemRepository.deleteById(id);
        return true;
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

            // 增加过滤条件 筛选  7<= id < 15 的数据
            boolQuery.filter(QueryBuilders.rangeQuery("id").gte(7).lt(15));

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

            // 随机排序
//            queryBuilder.withSort(SortBuilders.scriptSort(
//                    new Script("Math.random()"), ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.DESC));

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
