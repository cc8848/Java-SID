package com.quaint.demo.es.service.impl;

import com.quaint.demo.es.dto.article.AddDemoArticleReqDto;
import com.quaint.demo.es.dto.article.DemoArticleDto;
import com.quaint.demo.es.index.DemoArticleIndex;
import com.quaint.demo.es.mapper.DemoArticleMapper;
import com.quaint.demo.es.po.DemoArticlePO;
import com.quaint.demo.es.index.repository.DemoArticleRepository;
import com.quaint.demo.es.service.DemoArticleService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author quaint
 * @date 2020-01-03 23:36
 */
@Service
public class DemoArticleServiceImpl implements DemoArticleService {

    @Autowired
    DemoArticleMapper demoArticleMapper;

    @Autowired
    DemoArticleRepository demoArticleRepository;


    @Override
    public DemoArticleDto getDemoArticleList(DemoArticleDto.Param param) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // bool 查询条件
        BoolQueryBuilder bool = new BoolQueryBuilder();
        if (StringUtils.isNotEmpty(param.getTitle())){
            bool.should(QueryBuilders.matchQuery("title",param.getTitle()));
        }
        if (StringUtils.isNotEmpty(param.getContent())){
            bool.should(QueryBuilders.matchQuery("content",param.getContent()));
        }
        queryBuilder.withQuery(bool);

        // 数据排序, 随机排序, 随机后根据时间降序
        queryBuilder.withSort(SortBuilders.scriptSort(new Script("Math.random()"), ScriptSortBuilder.ScriptSortType.NUMBER));
        queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));

        // 数据设置分页, 不设置默认为10条数据
        if (param.esStandardization()){
            queryBuilder.withPageable(PageRequest.of(param.getPageNum(),param.getPageSize()));
        }

        // 查询
        Page<DemoArticleIndex> search = demoArticleRepository.search(queryBuilder.build());
        return processIndex2Dto(search);
    }

    /**
     * 将indexList 转换为 dtoList 并返回 dto
     * @param search pageList
     * @return dto
     */
    private DemoArticleDto processIndex2Dto(Page<DemoArticleIndex> search){

        // 将 search 查询出来的数据 转换为 dto list
        List<DemoArticleDto.Result> results = search.getContent().stream().map(idx -> {
            DemoArticleDto.Result result = new DemoArticleDto.Result();
            BeanUtils.copyProperties(idx, result);
            return result;
        }).collect(Collectors.toList());

        // 封装返回dto
        DemoArticleDto reDto = new DemoArticleDto();
        reDto.setArticleList(results);
        reDto.setTotal(search.getTotalElements());
        return reDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addDemoArticle(AddDemoArticleReqDto reqDto) {
        DemoArticlePO po = new DemoArticlePO();
        BeanUtils.copyProperties(reqDto,po);
        po.setCreateTime(LocalDateTime.now());
        demoArticleMapper.insert(po);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemoArticleDto.Result getDemoArticleById(Integer id) {
        DemoArticleDto.Result result = new DemoArticleDto.Result();
        demoArticleRepository.findById(id).ifPresent(idx -> BeanUtils.copyProperties(idx,result));
        demoArticleMapper.addPageViews(id);
        return result;
    }
}
