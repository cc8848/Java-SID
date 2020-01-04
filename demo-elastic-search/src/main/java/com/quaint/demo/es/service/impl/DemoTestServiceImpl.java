package com.quaint.demo.es.service.impl;

import com.quaint.demo.es.dto.DemoTestDto;
import com.quaint.demo.es.handler.impl.DemoTestHandler;
import com.quaint.demo.es.index.DemoTestIndex;
import com.quaint.demo.es.virtualdb.mapper.DemoTestMapper;
import com.quaint.demo.es.index.repository.DemoTestRepository;
import com.quaint.demo.es.service.DemoTestService;
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
public class DemoTestServiceImpl implements DemoTestService {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    DemoTestRepository demoTestRepository;

    @Autowired
    DemoTestMapper demoTestMapper;

    @Autowired
    DemoTestHandler demoTestHandler;


    @Override
    public Boolean addIndex() {
        // 可以根据类的信息在项目启动时会自动生成，也可以手动指定indexName和Settings -->  elasticsearchTemplate.putMapping;
        // 一版情况下使用不到
        elasticsearchTemplate.createIndex(DemoTestIndex.class);
        return true;
    }

    @Override
    public Boolean initIndexData() {
        demoTestHandler.refresh();
        return true;
    }

    @Override
    public Boolean addDocument(DemoTestDto dto) {

        DemoTestIndex demoTestIndex = new DemoTestIndex();
        BeanUtils.copyProperties(dto, demoTestIndex);
        demoTestIndex.setLike(false);
        demoTestIndex.setCreateTime(LocalDateTime.now());

        log.info("save : [{}]", demoTestIndex.toString());
        demoTestRepository.save(demoTestIndex);
        return true;
    }

    @Override
    public Boolean delDocumentById(Long id) {
        demoTestRepository.deleteById(id);
        return true;
    }

    @Override
    public DemoTestDto searchItemList(DemoTestDto.Param param) {

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
            Page<DemoTestIndex> searchPage = demoTestRepository.search(queryBuilder.build());

            // 封装dto
            DemoTestDto respDto = new DemoTestDto();
            respDto.setTotalCount(searchPage.getTotalElements());
            List<DemoTestDto.Result> resultList = searchPage.getContent().stream().map(demoTestIndex -> {
                DemoTestDto.Result result = new DemoTestDto.Result();
                BeanUtils.copyProperties(demoTestIndex, result);
                return result;
            }).collect(Collectors.toList());
            respDto.setItemList(resultList);

            return respDto;
        }
        return null;
    }


}
