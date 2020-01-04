package com.quaint.demo.es.handler.impl;

import com.quaint.demo.es.enums.DataType;
import com.quaint.demo.es.handler.AbstractDataChangeHandler;
import com.quaint.demo.es.index.DemoTestIndex;
import com.quaint.demo.es.virtualdb.mapper.DemoTestMapper;
import com.quaint.demo.es.po.DemoTestPo;
import com.quaint.demo.es.repository.DemoTestRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author quaint
 * @date 2020-01-03 15:17
 */
@Component
public class DemoTestHandler extends AbstractDataChangeHandler<Long> {

    @Autowired
    DemoTestMapper demoTestMapper;

    @Autowired
    DemoTestRepository demoTestRepository;

    @Override
    public void refresh() {
        demoTestRepository.deleteAll();
        // 应该分批处理, 作为demo 样例,查询全部
        List<DemoTestPo> itemList = demoTestMapper.getItemList();
        List<DemoTestIndex> indexList = itemList.stream().map(demoTestPo -> {
            DemoTestIndex index = new DemoTestIndex();
            BeanUtils.copyProperties(demoTestPo, index);
            return index;
        }).collect(Collectors.toList());
        demoTestRepository.saveAll(indexList);
    }

    @Override
    public void handleChange(Long pkId) {

        if (pkId != null){
            // 去数据库查询 数据
            DemoTestPo itemById = demoTestMapper.getItemById(pkId);
            DemoTestIndex demoTestIndex = new DemoTestIndex();
            BeanUtils.copyProperties(itemById, demoTestIndex);
            demoTestRepository.save(demoTestIndex);
        }

    }

    @Override
    public boolean support(DataType dataType) {
        return DataType.DEMO_TEST_TYPE.equals(dataType);
    }
}
